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
	private String currentMode;
	private Stage stage;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		Scene defaultGameScene = initializeGameScene(1280,720);
		scenes.put("principal", defaultGameScene);
		stage = arg0;
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
	/**
	 * sets the current window's "mode" to the specified mode
	 * @param mode the title of the mode into which to switch the window.
	 * @throws NonExistentScreenModeException if the mode is non existent.
	 */
	public void setScreenMode(String mode) throws NonExistentScreenModeException{
		currentMode = mode;
		if(scenes.containsKey(mode))
			stage.setScene(scenes.get(mode));
		else
			throw new NonExistentScreenModeException();
	}
}
