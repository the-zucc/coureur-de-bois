package app;

import java.util.Hashtable;

import entity.Player;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.ControlerLoop;
import model.Model;

public class Main extends Application {

	private static Main instance;
	private Hashtable<String, SubScene> subScenes;
	private Hashtable<String, Scene> scenes;
	private Hashtable<String, Node> debugNodes;
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
	public SubScene getSubScene(String mode) {
		return subScenes.get(mode);
	}
	public Node getDebugNode(String nodeKey) {
		return debugNodes.get(nodeKey);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		instance = this;
		stage = arg0;//so that the window is accessible at all times
		stage.setAlwaysOnTop(false);
		
		//initializing the scene
		int width = 640;
		int height = 480;
		scenes = new Hashtable<String, Scene>();
		subScenes = new Hashtable<String, SubScene>();
		debugNodes = new Hashtable<String, Node>();
		SubScene gameSubScene = initializeGameSubScene(width, height);
		Scene defaultGameScene = initializeGameScene(width, height, gameSubScene);
		subScenes.put("principal", gameSubScene);
		scenes.put("principal", defaultGameScene);
		
		//create a debug Model instance with a number of mobs
		Model.newDebugInstance(200, 200);
		
		//for debug and testing purposes
		Label label = new Label("player position");
		label.setTranslateX(50);
		label.setTranslateY(50);
		label.setTextFill(Color.BLUE);
		label.setScaleX(2);
		label.setScaleY(2);
		debugNodes.put("label_position_player", label);
		((Group)scenes.get("principal").getRoot()).getChildren().add(label);
		
		
		
		//floor, debug only
		//create the floor box
		Group floor = Model.getInstance().getFloor();
		//setting its material
		PhongMaterial floorMaterial = new PhongMaterial();
		floorMaterial.setDiffuseColor(Color.GREEN);
		for(Node n:floor.getChildren()){
			if(n instanceof Box) {
				((Box)n).setMaterial(floorMaterial);
			}
		}
		
		//adding it to the scene
		((Group)subScenes.get("principal").getRoot()).getChildren().add(floor);
		
		//starting the controler loop
		ControlerLoop loop = new ControlerLoop();
		loop.start();
		
		Model.getInstance().getCurrentPlayer().getElement3D().getChildren().add(subScenes.get("principal").getCamera());
		Animation animation = new Transition() {
			{
				setCycleDuration(Duration.millis(500));
			}
			protected void interpolate(double frac) {
				double isometricValueZ = -500;
				double isometricValueY = -500;
				Camera currentCamera = Main.this.subScenes.get("principal").getCamera();

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
		bindGameControls(subScenes.get("principal").getScene());
		
		//set the scene to the game scene
		arg0.setScene(defaultGameScene);
		
		//set fullscreen
		//arg0.setFullScreen(true);
		
		//show the screen
		arg0.show();
	}

	/**
	 * this function 
	 * @param width the width of the scene
	 * @param height the height of the scene
	 * @return a game scene of size width by height
	 */
	public Scene initializeGameScene(double width, double height, SubScene subScene) {
		Group root = new Group();
		root.getChildren().add(subScene);
		Scene returnVal = new Scene(root, width, height);
		return returnVal;
	}
	public SubScene initializeGameSubScene(int width, int height) {
		Group root3D = new Group();
		SubScene screenScene = new SubScene(root3D, width, height, true, SceneAntialiasing.DISABLED);
		PerspectiveCamera camera = new PerspectiveCamera(true);
		camera.setNearClip(0.1);
		camera.setFarClip(30000);
		camera.setRotationAxis(Rotate.X_AXIS);
		camera.setRotate(-45);
		camera.setFieldOfView(60);
		screenScene.setCamera(camera);
		return screenScene;
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
		if (subScenes.containsKey(mode))
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
				else if(code.equals(KeyCode.F11)) {
					if(!stage.isFullScreen())
						stage.setFullScreen(true);
					else
						stage.setFullScreen(false);
					updateScreenResolution();
				}
				else if(code.equals(KeyCode.ESCAPE)) {
					updateScreenResolution();
				}
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
	public void updateScreenResolution() {
		subScenes.get("principal").setWidth(stage.getWidth());
		subScenes.get("principal").setHeight(stage.getHeight());
	}
	
}
