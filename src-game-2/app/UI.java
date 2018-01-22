package app;

import java.util.Hashtable;

import entity.Player;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import util.Updateable;
import visual.InformationPane;
import visual.PlayerComponent;

public class UI extends Scene implements Updateable{
	//for singleton implementation
	private static UI instance;
	
	public static UI getInstance() {
		if(instance == null) {
			instance = new UI(new Group(), 1280, 720, false,  SceneAntialiasing.DISABLED);
		}
		return instance;
	}
	
	public static UI newInstance(double width, double height, SceneAntialiasing antialiasing) {
		instance = new UI(new Group(), 1280, 720, false, antialiasing);
		return instance;
	}
	
	//instance variables
	private Hashtable<String, Node> uINodes;
	private Hashtable<String, InformationPane> informationPanes;
	private Group uIRoot;
	private SubScene gameViewPort;
	private double mouseX;
	private double mouseY;
	
	public UI(Parent root, double width, double height, boolean depthBuffer, SceneAntialiasing antiAliasing) {
		super(root, width, height, depthBuffer, SceneAntialiasing.DISABLED);
		uINodes = new Hashtable<String, Node>();
		informationPanes = new Hashtable<String, InformationPane>();
		
		uIRoot = (Group)root;//the root of the whole UI. it holds the UI labels and all, as well as the game subscene
		
		gameViewPort = GameScene.getInstance();//the subscene containing all of the 3D elements.
		
		uIRoot.getChildren().add(gameViewPort);//add the game subscene to the UI
		
		bindGameControls(this);
	}
	public void addUiNode(Node element) {
		if(element instanceof InformationPane)
			informationPanes.put(element.getId(), (InformationPane)element);
		uINodes.put(element.getId(), element);
		uIRoot.getChildren().add(element);
	}
	public void removeUiNode(String id) {
		uIRoot.getChildren().remove(uINodes.get(id));
		uINodes.remove(id);
		if(informationPanes.containsKey(id))
			informationPanes.remove(id);
	}
	@Override
	public void update(double deltaTime) {
		//System.out.println("X:"+Controller.getApplicationWindow().getX()+" Y:"+Controller.getApplicationWindow().getY());
		for(InformationPane p:informationPanes.values()) {
			p.update(deltaTime);
		}
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
				mouseX = arg0.getSceneX();
				mouseY = arg0.getSceneY();
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
				mouseX = arg0.getSceneX();
				mouseY = arg0.getSceneY();
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
					System.out.println(Model.getInstance().getElement(id).getPosition());
					//Model.getInstance().getCurrentPlayer().attack(Model.getInstance().getElement(id));
				}catch(NullPointerException npe) {
					System.out.println("No element here");
				}
			}
			
		});
	}
	public double getMouseX() {
		return mouseX;
	}
	public double getMouseY() {
		return mouseY;
	}
}