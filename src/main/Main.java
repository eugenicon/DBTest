package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application{

	public static void main(String[] args) {

		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("../res/MainScene.fxml")));
		
		primaryStage.setScene(scene);
		
		primaryStage.getIcons().add(new Image("res/MainIco.png"));
		
		primaryStage.show();
	}
	
}
