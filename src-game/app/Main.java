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
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
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
		// this is here so that the window instance can be accessible at all times;
		instance = this;
		
		//initializing the scene
		Scene defaultGameScene = initializeGameScene(1280, 720);
		scenes = new Hashtable<String, Scene>();
		scenes.put("principal", defaultGameScene);
		
		//create a debug Model instance with a number of mobs
		Model.newDebugInstance(2000);
		
		stage = arg0;//so that the window is accessible at all times
		
		//floor, debug only
		//create the floor box
		Box floor = Model.getInstance().getFloor();
		//setting its material
		PhongMaterial floorMaterial = new PhongMaterial();
		floorMaterial.setDiffuseColor(Color.GREEN);
		floor.setMaterial(floorMaterial);
		
		//adding it to the scene
		((Group) defaultGameScene.getRoot()).getChildren().add(floor);
		
		//starting the controler loop
		ControlerLoop loop = new ControlerLoop();
		loop.start();
		//setting the Id of the camera
		defaultGameScene.getCamera().setId("camera");
		
		Model.getInstance().getCurrentPlayer().getElement3D().getChildren().add(defaultGameScene.getCamera());
		Animation animation = new Transition() {
			{
				setCycleDuration(Duration.millis(500));
			}
			protected void interpolate(double frac) {
				double isometricValueZ = -400;
				double isometricValueY = -400;
				Camera currentCamera = Main.this.scenes.get("principal").getCamera();

				// currentCamera.setTranslateX(isometricValue*frac);
				currentCamera.setTranslateY(isometricValueY * frac);
				currentCamera.setTranslateZ(isometricValueZ * frac);

				// currentCamera.setRotationAxis(Rotate.Y_AXIS);
				// currentCamera.setRotate(80*frac);
				currentCamera.setRotationAxis(Rotate.X_AXIS);
				currentCamera.setRotate(-45 * frac);
				// currentCamera.setRotationAxis(Rotate.Z_AXIS);
				// currentCamera.setRotate(0*frac);
			}
		};
		animation.play();
		//binds the game controls so that the player can move, jump, etc
		bindGameControls(scenes.get("principal"));
		
		//set the scene to the game scene
		arg0.setScene(defaultGameScene);
		
		//set fullscreen
		arg0.setFullScreen(true);
		
		//show the screen
		arg0.show();
	}

	/**
	 * this function 
	 * @param width the width of the scene
	 * @param height the height of the scene
	 * @return a game scene of size width by height
	 */
	public Scene initializeGameScene(double width, double height) {
		Group root = new Group();
		PerspectiveCamera camera = new PerspectiveCamera(true);
		Scene returnVal = new Scene(root, width, height, true, SceneAntialiasing.BALANCED);
		camera.setNearClip(0.1);
		camera.setFarClip(30000);
		camera.setRotationAxis(Rotate.X_AXIS);
		camera.setRotate(-45);
		camera.setFieldOfView(74);
		returnVal.setCamera(camera);
		return returnVal;
	}
	
	/**
	 * sets the current window's "mode" to the specified mode
	 * 
	 * @param mode
	 *            the title of the mode into which to switch the window.
	 * @throws NonExistentScreenModeException
	 *             if the mode is non existent.
	 */
	public void setScreenMode(String mode) throws NonExistentScreenModeException {
		currentMode = mode;
		if (scenes.containsKey(mode))
			stage.setScene(scenes.get(mode));
		else
			throw new NonExistentScreenModeException();
	}
	
	/**
	 * this function sets the game controls for the specified scene. this should only be used on a scene where one would want the player to move, jump, etc.
	 * @param scene the scene for which the conrols are to be binded.
	 */
	public void bindGameControls(Scene scene) {
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
				else if(code.equals(KeyCode.F11))
					if(!stage.isFullScreen())
						stage.setFullScreen(true);
					else
						stage.setFullScreen(false);
//				else if(code.equals(KeyCode.DOWN)) {
//					PerspectiveCamera camera = (PerspectiveCamera)scenes.get("principal").getCamera();
//					camera.setRotationAxis(Rotate.X_AXIS);
//					camera.setRotate(camera.getRotate()-5);
//				}
			}
		});
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				//System.out.println(arg0.getSceneX()+" "+arg0.getSceneY());
			}
			
		});
	}
	
}
