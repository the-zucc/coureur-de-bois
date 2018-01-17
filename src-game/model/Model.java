package model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

import app.Main;
import collision.CollisionBox;
import collision.CollisionMatrix;
import entity.DefaultMob;
import entity.GameElement;
import entity.Player;
import entity.Refreshable;
import entity.Tree;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Box;

public class Model implements Refreshable{
	private static Model instance;
	private ArrayList<Refreshable> refreshables;
	private ArrayList<GameElement> gameElements;
	private Hashtable<String, GameElement> gameElementsHashtable;
	private Player currentPlayer;
	private CollisionMatrix collisionMatrix;
	
	//all nodes of the scene. to get their positions and such.
	private ArrayList<Node> gameElementNodes;
	
	//debug, remove this asap
	public static int minCoordDebug = -1000;
	public static int maxCoordDebug = 1000;
	private Model() {
		refreshables = new ArrayList<Refreshable>();
		//currentPlayer = new Player(Point3D.ZERO);
		collisionMatrix = new CollisionMatrix(maxCoordDebug-minCoordDebug, maxCoordDebug-minCoordDebug);
	}
	private Model(int nombreMobsDebug, int nombreArbresDebug) {
		collisionMatrix = new CollisionMatrix(maxCoordDebug-minCoordDebug,maxCoordDebug-minCoordDebug);
		refreshables = new ArrayList<Refreshable>();
		gameElements = new ArrayList<GameElement>();
		gameElementsHashtable = new Hashtable<String, GameElement>();
		int min = minCoordDebug;
		int max = maxCoordDebug;
		//loop for generating multiple random mobs, for debug purposes
		for(int i = 0; i < nombreMobsDebug; i++) {
			double x = (double)ThreadLocalRandom.current().nextInt(min, max);
			double z = (double)ThreadLocalRandom.current().nextInt(min, max);
			Point3D pos = new Point3D(x, 0, z);
			DefaultMob newMob = new DefaultMob(pos);
			double xTarget = (double)ThreadLocalRandom.current().nextInt(min, max);
			double zTarget = (double)ThreadLocalRandom.current().nextInt(min, max);
			Point3D target = new Point3D(xTarget, 0, zTarget);
			newMob.targetPoint(target);
			double angle = (double)ThreadLocalRandom.current().nextInt(0, 360 + 1);
			
			gameElements.add(newMob);
			gameElementsHashtable.put(newMob.getId(), newMob);
		}
		//loop for generating multiple tree objects, for debug purposes
		for(int i = 0; i < nombreArbresDebug; i++) {
			double x = (double)ThreadLocalRandom.current().nextInt(min, max);
			double z = (double)ThreadLocalRandom.current().nextInt(min, max);
			Point3D pos = new Point3D(x, 0, z);
			Tree newTree = new Tree(pos);
			gameElements.add(newTree);
			gameElementsHashtable.put(newTree.getId(), newTree);
		}
		
		//generating the player
		double x = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
		double z = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
		Point3D pos = new Point3D(x, 0, z);
		currentPlayer = new Player(pos);
		gameElements.add(currentPlayer);
		gameElementsHashtable.put(currentPlayer.getId(), currentPlayer);
	}
	public static Model getInstance() {
		if(instance == null)
			instance = new Model();
		return instance;
	}
	public static Model newDebugInstance(int nombreMobsDebug, int nombreArbresDebug) {
		instance = new Model(nombreMobsDebug, nombreArbresDebug);
		return instance;
	}
	public GameElement getGameElement(String id) {
		return gameElementsHashtable.get(id);
	}
	public Group getFloor() {
		return collisionMatrix.getFloor();
	}
	public Bounds getFloorBounds() {
		return collisionMatrix.getFloorBounds();
	}
	
	/**
	 * this function refreshes all the elements of the object's refreshables ArrayList as well as the currentPlayer object.
	 * @param deltaTime 
	 */
	public void update(double deltaTime) {
		for(GameElement ge:gameElements) {
			ge.update(deltaTime);
		}
	}
	/**
	 * this function refreshes all the elements of the 3D scene for the game Environment.
	 */
	public void updateGraphics() {
		for(GameElement ge:gameElements) {
			Platform.runLater(() -> {
				ge.updateElement3DPosition();				
			});
			
		}
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public CollisionMatrix getFloorMatrix(){
		return collisionMatrix;
	}
	public ArrayList<Refreshable> getRefreshables(){
		return refreshables;
	}
	public ArrayList<GameElement> getGameElements(){
		return gameElements;
	}
	public void deleteGameElement(GameElement ge) {
		gameElements.remove(ge);
		gameElementsHashtable.remove(ge.getId());
		Group element3D = ge.getElement3D();
		((Group)element3D.getParent()).getChildren().remove(element3D);
		CollisionBox cb = ge.getCollisionBox();
		collisionMatrix.removeFromDivision(cb.getMapDivisionRow(), cb.getMapDivisionColumn(), cb);
	}
	
}
