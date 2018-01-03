package app;

import java.util.Hashtable;

import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class Main extends Application {
	private Hashtable<String, Scene> scenes;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Scene defaultGameScene = initializeGameScene(1280,720);
		scenes.put("principal", defaultGameScene);
		
		arg0.setScene(defaultGameScene);
		arg0.show();
	}
	public Scene initializeGameScene(double width, double height) {
		Group root = new Group();
		PerspectiveCamera camera = new PerspectiveCamera(true);
		Scene returnVal = new Scene(root, width, height);
		camera.setNearClip(10);
		camera.setFarClip(3000);
		camera.setRotationAxis(Rotate.X_AXIS);
		camera.setRotate(-45);
		returnVal.setCamera(camera);
		return returnVal;
	}

}
