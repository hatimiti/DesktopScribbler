package com.github.hatimiti.scribbler;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		Stage transparentStage = new Stage(StageStyle.TRANSPARENT);
		Hatimiti_Stage stage = new Hatimiti_Stage(transparentStage);
		stage.show("scribble.fxml");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

class Hatimiti_Stage {
	
	private Stage stage;
	private BaseController controller;
	
	Hatimiti_Stage(Stage stage) {
		this.stage = stage;
	}
	
	public void show(String fxml) {
		Scene scene = createScene(fxml);
		stage.setScene(scene);
		controller.onLoadCompleted();
		stage.show();
	}
	
	private Scene createScene(String fxml) {
		return new Scene(loadFXML(fxml));
	}

	private Parent loadFXML(String fxml) {
		Parent root;
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource(fxml));
		try {
			root = (Parent) loader.load();
			controller = ((BaseController) loader.getController());
			controller.setStage(stage);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return root;
	}
	
}
