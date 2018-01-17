package app;

import java.util.Hashtable;

import entity.GameElement;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.transform.Rotate;
import util.Updateable;
import visual.GameComponent;

public class GameScene extends Scene implements Updateable {
	//for singleton implementation
	private static GameScene instance;
	public static GameScene getInstance() {
		if(instance == null) {
			instance = new GameScene(new Group(), 1280, 720, SceneAntialiasing.BALANCED);
		}
		return instance;
	}
	public static GameScene newInstance(double width, double height, SceneAntialiasing antialiasing) {
		instance = new GameScene(new Group(), 1280, 720, antialiasing);
		return instance;
	}
	//instance variables
	private Hashtable<String, GameComponent> gameComponents;
	private Group uIRoot;
	private Group gameEnvRoot;
	private SubScene gameViewPort;
	private PerspectiveCamera gameCamera;
	
	private GameScene(Parent arg0, double arg1, double arg2, SceneAntialiasing arg4) {
		//create the scene
		super(arg0, arg1, arg2, false, SceneAntialiasing.DISABLED);
		
		//defining custom class attributes
		uIRoot = (Group)arg0;//the root of the whole UI. it holds the UI labes and all, as well as the game subscene
		gameEnvRoot = new Group();//the root of the game environment (passed as argument to the subscene below)
								//it holds all the game elements' components
		gameViewPort = new SubScene(gameEnvRoot, arg1, arg2, true, arg4);//the subscene containing all of the 3D elements.
		uIRoot.getChildren().add(gameViewPort);//
	}
	/**
	 * updates the game components of the game subscene
	 */
	public void updateGraphics(double deltaTime) {
		for(GameElement ge:Model.getInstance().getGameElements()) {
			GameComponent component = gameComponents.get(ge.getId()); 
			if(component instanceof Updateable) {
				((Updateable) component).update(deltaTime);
			}
		}
	}
	/**
	 * binds the game controls to the specified scene
	 * @param scene the scene to which bind the controls
	 */
	private void bindGameControls(Scene scene) {
		
	}
	/**
	 * updates the specified {@link GameElement}'s corresponding element's position and state.
	 * @param ge the {@link GameElement} of which to update the component
	 */
	private void updateComponentOfElement(GameElement ge) {
		GameComponent component = gameComponents.get(ge.getId());
		if(component == null) {
			component = ge.buildComponent();
			gameComponents.put(ge.getId(), component);
			gameEnvRoot.getChildren().add(component);
		}
		Point3D position = ge.getPosition();
		component.setTranslateX(position.getX());
		component.setTranslateY(position.getY());
		component.setTranslateZ(position.getZ());		
	}
	/**
	 * returns a reference to the component corresponding to the specified component/element ID.
	 * @param id the id of the component
	 * @return the component corresponding to the specified id
	 */
	private GameComponent getComponent(String id) {
		return gameComponents.get(id);
		
	}
	
	@Override
	public void update(double deltaTime) {
		updateGraphics(deltaTime);
	}
	/**
	 * Builds the {@link PerspectiveCamera} of the game scene.
	 * @return the camera to add to the desired scene or subscene
	 */
	private static PerspectiveCamera buildCamera() {
		double distance = Controller.debug_gameCameraDistance;
		PerspectiveCamera returnVal = new PerspectiveCamera(true);
		returnVal.setTranslateY(-distance);
		returnVal.setTranslateZ(-distance);
		returnVal.setRotationAxis(Rotate.X_AXIS);
		returnVal.setRotate(-45);
		return returnVal;
	}
}
