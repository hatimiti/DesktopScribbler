package com.github.hatimiti.scribbler;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class ScribbleController extends BaseController implements Initializable {

	@FXML private TextField display;
	@FXML private Slider lineWidthSlider;
	@FXML private Canvas canvas;
	@FXML private ColorPicker lineColorPicker;
	
	private GraphicsContext gc;
	private AtomicBoolean isMousePrimaryDragged = new AtomicBoolean(false);
	private AtomicReference<Double> lineWidth = new AtomicReference<>(1.0);
	private AtomicReference<Color> lineColor = new AtomicReference<>(Color.RED);
	private Point2D preDrawdPoint = Point2D.ZERO;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gc = canvas.getGraphicsContext2D();
		canvas.setOnMousePressed(this::onMousePressed);
		canvas.setOnMouseDragged(this::onMouseDragged);
		canvas.setOnMouseReleased(this::onMouseReleased);
		canvas.setMouseTransparent(false);

		lineWidthSlider.setMin(1.0);
		lineWidthSlider.setMax(20.0);
		lineWidthSlider.valueProperty()
			.addListener(this::onValueOfLineWidthSliderChanged);
		
		lineColorPicker.valueProperty().set(lineColor.get());
		lineColorPicker.valueProperty()
			.addListener(this::onValueOfLineColorPickerChanged);
	}
	
	@Override
	public void onLoadCompleted() {
		stage.getScene().setFill(null);
		//stage.setAlwaysOnTop(true);
		fullScreen();
	}

	private void onValueOfLineWidthSliderChanged(
			ObservableValue<? extends Number> o, Number oldValue, Number newValue) {
		lineWidth.set(newValue.doubleValue());
	}
	
	private void onValueOfLineColorPickerChanged(
			ObservableValue<? extends Color> o, Color oldValue, Color newValue) {
		lineColor.set(newValue);
	}
	
	private void onMousePressed(MouseEvent evt) {
		
		if (MouseButton.PRIMARY != evt.getButton()) {
			return;
		}
		isMousePrimaryDragged.set(true);
		preDrawdPoint = createCurrentMousePoint(evt);
		doMouseDrawing(preDrawdPoint, preDrawdPoint);
	}

	private void onMouseDragged(MouseEvent evt) {
		if (!isMousePrimaryDragged.get()) {
			return;
		}
		Point2D current = createCurrentMousePoint(evt);
		doMouseDrawing(preDrawdPoint, current);
		preDrawdPoint = current;
	}
	
	private void doMouseDrawing(Point2D start, Point2D end) {
		gc.setStroke(lineColor.get());
		gc.setLineWidth(lineWidth.get());
		gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());
	}
	
	private void onMouseReleased(MouseEvent evt) {
		if (MouseButton.PRIMARY != evt.getButton()) {
			return;
		}
		isMousePrimaryDragged.set(false);
	}

	@FXML
	protected void onCloseButtonClicked(ActionEvent evt) {
		// FIXME https://bugs.openjdk.java.net/browse/JDK-8087498
		Platform.exit();
	}
	
	@FXML
	protected void onFullScreenButtonClicked(ActionEvent evt) {
		fullScreen();
	}
	
	@FXML
	protected void onClearButtonClicked(ActionEvent evt) {
		canvas.getGraphicsContext2D()
			.clearRect(0, 0, stage.getWidth(), stage.getHeight());
	}
	
	@Override
	public void fullScreen() {
		super.fullScreen();
		canvas.setWidth(stage.getWidth());
		canvas.setHeight(stage.getHeight());
	}

	private Point2D createCurrentMousePoint(MouseEvent evt) {
		return new Point2D(evt.getX(), evt.getY());
	}
	
	public static void log(Object obj) {
		System.out.println(obj);
	}

	public static void log(String format, Object... obj) {
		System.out.format(format, obj).println();
	}
}
