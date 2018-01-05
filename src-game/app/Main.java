package app;

import java.util.Hashtable;

import entity.Player;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.ControlerLoop;
import model.Model;

public class Main extends Application {
	
	private static Main instance;
	private Hashtable<String, Scene> scenes;
	private String currentMode;
	private Stage stage;
	
	public static Main getInstance() {
		return instance;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	public Scene getScene(String mode) {
		return scenes.get(mode);
	}
	@Override
	public void start(Stage arg0) throws Exception {
		//this is here so that the window instance can be accessible at all times;
		instance = this;
		Scene defaultGameScene = initializeGameScene(1280,720);
		scenes = new Hashtable<String, Scene>();
		scenes.put("principal", defaultGameScene);
		Model.newDebugInstance(10);
		stage = arg0;
		((Group)defaultGameScene.getRoot()).getChildren().add(new Box(1000, 1, 1000));
		ControlerLoop loop = new ControlerLoop();
		loop.start();
		Model.getInstance().getCurrentPlayer().getElement3D().getChildren().add(defaultGameScene.getCamera());
		Animation animation = new Transition() {
			{
				setCycleDuration(Duration.millis(500));
			}
			protected void interpolate(double frac) {
				double isometricValueZ = -400;
				double isometricValueY = -800;
				Camera currentCamera = Main.this.scenes.get("principal").getCamera();
				
				//currentCamera.setTranslateX(isometricValue*frac);
				currentCamera.setTranslateY(isometricValueY*frac);
				currentCamera.setTranslateZ(isometricValueZ*frac);
				
				//currentCamera.setRotationAxis(Rotate.Y_AXIS);
				//currentCamera.setRotate(80*frac);
				currentCamera.setRotationAxis(Rotate.X_AXIS);
				currentCamera.setRotate(-60*frac);
				//currentCamera.setRotationAxis(Rotate.Z_AXIS);
				//currentCamera.setRotate(0*frac);
			}
		};
		animation.play();
		setGameControls(scenes.get("principal"));
		arg0.setScene(defaultGameScene);
		arg0.setFullScreen(true);
		arg0.show();
	}
	public Scene initializeGameScene(double width, double height) {
		Group root = new Group();
		PerspectiveCamera camera = new PerspectiveCamera(true);
		Scene returnVal = new Scene(root, width, height, true, SceneAntialiasing.BALANCED);
		camera.setNearClip(0.1);
		camera.setFarClip(3000);
		camera.setRotationAxis(Rotate.X_AXIS);
		camera.setRotate(-45);
		camera.setFieldOfView(74);
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
	public void setGameControls(Scene scene) {
		scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent arg0) {
				KeyCode code = arg0.getCode();
				Player player = Model.getInstance().getCurrentPlayer();
				if(code.equals(KeyCode.W))
					player.setUp(true);
				else if(code.equals(KeyCode.A))
					player.setLeft(true);
				else if(code.equals(KeyCode.S))
					player.setDown(true);
				else if(code.equals(KeyCode.D))
					player.setRight(true);
				else if(code.equals(KeyCode.SHIFT))
					player.setIsRunning(true);
				else if(code.equals(KeyCode.SPACE))
					player.jump();
			}
			
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				KeyCode code = event.getCode();
				Player player = Model.getInstance().getCurrentPlayer();
				if(code.equals(KeyCode.W))
					player.setUp(false);
				else if(code.equals(KeyCode.A))
					player.setLeft(false);
				else if(code.equals(KeyCode.S))
					player.setDown(false);
				else if(code.equals(KeyCode.D))
					player.setRight(false);
				else if(code.equals(KeyCode.SHIFT))
					player.setIsRunning(false);
			}
			
		});
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				System.out.println(arg0.getSceneX()+" "+arg0.getSceneY());
			}
			
		});
	}
}
