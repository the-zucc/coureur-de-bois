package app;

import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

import entity.GameElement;
import entity.Player;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import util.Updateable;
import visual.GameComponent;
import visual.PlayerComponent;

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
	private static double floorSectionWidth = 5;
	private static double floorSectionHeight = 5;
	
	
	private GameScene(Parent arg0, double arg1, double arg2, SceneAntialiasing arg4) {
		//create the scene
		super(arg0, arg1, arg2, false, SceneAntialiasing.DISABLED);
		
		//defining the gameComponents Hashtable
		gameComponents = new Hashtable<String, GameComponent>();
		
		//defining custom class attributes
		uIRoot = (Group)arg0;//the root of the whole UI. it holds the UI labes and all, as well as the game subscene
		
		gameEnvRoot = new Group();//the root of the game environment (passed as argument to the subscene below)
								//it holds all the game elements' components
		
		gameViewPort = new SubScene(gameEnvRoot, arg1, arg2, true, arg4);//the subscene containing all of the 3D elements.
		uIRoot.getChildren().add(gameViewPort);//add the game subscene to the UI
		
		gameCamera = buildGameCamera();//build the game camera
		
		gameViewPort.setCamera(gameCamera);//setup the game scene to use the built camera
		
		//setup the floor
		Group floor = buildFloor((int)(Model.getInstance().getMapWidth()/floorSectionWidth),(int)(Model.getInstance().getMapWidth()/floorSectionHeight), (int)floorSectionWidth, (int)floorSectionHeight);
		floor.setId("id_floor");
		//add the floor to the game environment
		gameEnvRoot.getChildren().add(floor);
		
		//debug
		gameEnvRoot.getChildren().add(new Label("2fdshajflhdsjaklhjkl"));
		bindGameControls(this);
	}
	/**
	 * updates the game components of the game subscene
	 */
	public void updateGraphics(double deltaTime) {
		for(GameElement ge:Model.getInstance().getGameElements()) {
			updateComponentOfElement(ge);
			//((Updateable) component).update(deltaTime);
		}
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
		if(component instanceof Updateable) {
			Point3D position = ge.getPosition();
			component.setTranslateX(position.getX());
			component.setTranslateY(position.getY());
			component.setTranslateZ(position.getZ());
		}
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
	private static PerspectiveCamera buildGameCamera() {
		double distance = Controller.debug_gameCameraDistance;
		PerspectiveCamera returnVal = new PerspectiveCamera(true);
		returnVal.setNearClip(0.1);
		returnVal.setFarClip(4000);
		returnVal.setTranslateY(-distance);
		returnVal.setTranslateZ(-distance);
		returnVal.setRotationAxis(Rotate.X_AXIS);
		returnVal.setRotate(-45);
		return returnVal;
	}
	
	/**
	 * returns the game camera, to be able to modify the angle of the camera from anywhere within the program
	 * @return the {@link PerspectiveCamera} of the game environment.
	 */
	public PerspectiveCamera getGameCamera() {
		return gameCamera;
	}
	
	public SubScene getGameViewPort() {
		return gameViewPort;
	}
	
	private static Group buildFloor(int cols, int rows, int colwidth, int rowheight) {
		Group returnVal = new Group();
		double height = rowheight;
		double width = colwidth;
		float[][] yValues = new float[rows][cols];
		for(int z = 0; z < rows; z++) {
			for(int x = 0; x < cols; x++) {
				float y = (float)ThreadLocalRandom.current().nextDouble()*50;
				yValues[z][x]=y;
			}
		}
		for(int z = 0; z < rows-1; z++) {
			TriangleMesh mesh = new TriangleMesh();
			for(int x = 0; x < cols; x++) {
				mesh.getPoints().addAll((float)((-x*width)-(-Model.getInstance().getMapWidth()/2)), yValues[z][x], (float)(((-z)*height)-(-Model.getInstance().getMapHeight()/2)));
				mesh.getPoints().addAll((float)((-x*width)-(-Model.getInstance().getMapWidth()/2)), yValues[z+1][x], (float)(((-(z+1))*height)-(-Model.getInstance().getMapHeight()/2)));
			}
			mesh.getTexCoords().addAll(0,0);
			for(int i=2;i<cols*2;i+=2) {  //add each segment
		        //Vertices wound counter-clockwise which is the default front face of any Triange
		        //These triangles live on the frontside of the line facing the camera
		        mesh.getFaces().addAll(i,0,i-2,0,i+1,0); //add primary face
		        mesh.getFaces().addAll(i+1,0,i-2,0,i-1,0); //add secondary Width face
		        //Add the same faces but wind them clockwise so that the color looks correct when camera is rotated
		        //These triangles live on the backside of the line facing away from initial the camera
		        mesh.getFaces().addAll(i+1,0,i-2,0,i,0); //add primary face
		        mesh.getFaces().addAll(i-1,0,i-2,0,i+1,0); //add secondary Width face
		    }
			MeshView meshView = new MeshView(mesh);
			meshView.setDrawMode(DrawMode.FILL);
			
			PhongMaterial material = new PhongMaterial();
//			int choice = z%4;
//			switch(choice) {
//			case 0:
//				material.setDiffuseColor(Color.BROWN);
//				break;
//			case 1:
//				material.setDiffuseColor(Color.CORAL);
//				break;
//			case 2:
//				material.setDiffuseColor(Color.BLUEVIOLET);
//				break;
//			case 3:
//				material.setDiffuseColor(Color.CYAN);
//				break;
//			}
			material.setDiffuseColor(Color.GREEN);
			meshView.setMaterial(material);
			meshView.setTranslateX(0);
			meshView.setTranslateY(-25);
			meshView.setTranslateZ(0);
			returnVal.getChildren().add(meshView);
		}
		
		return returnVal;
	}
	/**
	 * this function sets the game controls for the specified scene. this should only be used on a scene where one would want the player to move, jump, etc.
	 * @param scene the scene for which the conrols are to be binded.
	 */
	public void bindGameControls(Scene scene) {
		scene.setCursor(new ImageCursor(new Image("res/cursor.png")));
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
//				else if(code.equals(KeyCode.SPACE))
//					player.jump();
				arg0.consume();
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
					if(!Controller.getApplicationWindow().isFullScreen()) {
						Controller.getApplicationWindow().setFullScreen(true);
						Controller.updateScreenResolution();
					}
					else
						Controller.exitFullScreen();
					
				}
				else if(code.equals(KeyCode.ESCAPE)) {
					Controller.exitFullScreen();
				}
//				else if(code.equals(KeyCode.DOWN)) {
//					PerspectiveCamera camera = (PerspectiveCamera)scenes.get("principal").getCamera();
//					camera.setRotationAxis(Rotate.X_AXIS);
//					camera.setRotate(camera.getRotate()-5);
//				}
				event.consume();
			}
		});
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
			
			@Override
			public void handle(MouseEvent arg0) {
				double y = arg0.getSceneY() - Controller.getApplicationWindow().getHeight()/2;
				double x = arg0.getSceneX() - Controller.getApplicationWindow().getWidth()/2;
				Group player = ((PlayerComponent)GameScene.getInstance().getComponent(Model.getInstance().getCurrentPlayer().getId())).getPlayerNode();
				player.setRotationAxis(Rotate.Y_AXIS);
				player.setRotate(Math.toDegrees(Math.atan2(y, x))+90);
				
				arg0.consume();
			}
			
		});
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				
				double y = arg0.getSceneY() - Controller.getApplicationWindow().getHeight()/2;
				double x = arg0.getSceneX() - Controller.getApplicationWindow().getWidth()/2;
				Group player = ((PlayerComponent)GameScene.getInstance().getComponent(Model.getInstance().getCurrentPlayer().getId())).getPlayerNode();
				player.setRotationAxis(Rotate.Y_AXIS);
				player.setRotate(Math.toDegrees(Math.atan2(y, x))+90);
				
				arg0.consume();
			}
			
		});
		scene.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				try {
					Node picked = arg0.getPickResult().getIntersectedNode(); 
					String id = picked.getId();
					while(id == null) {
						picked = picked.getParent();
						id = picked.getId();
					}
					//System.out.println(Model.getInstance().getGameElement(id).getPosition());
					//Model.getInstance().getCurrentPlayer().attack(Model.getInstance().getElement(id));
				}catch(NullPointerException npe) {
					System.out.println("No element here");
				}
			}
			
		});
	}
	public void setCameraOnPlayer(Camera camera, String id) {
		gameComponents.get(id).getChildren().add(camera);
	}
	
}
