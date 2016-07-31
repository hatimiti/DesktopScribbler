package com.github.hatimiti.scribbler;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public abstract class BaseController {

	protected Stage stage;

	void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public abstract void onLoadCompleted();
	
	public void fullScreen() {
		// FIXME https://twitter.com/tomo_taka01/status/529134708248952833
//		stage.setFullScreen(!stage.isFullScreen());
		
		stage.setX(0);
		stage.setY(25);
		
		Rectangle2D d = Screen.getPrimary().getVisualBounds();
		stage.setWidth(d.getWidth());
		stage.setHeight(d.getHeight());
	}
}
