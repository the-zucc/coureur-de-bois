package model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import app.Main;
import entity.DefaultMob;
import entity.GameElement;
import entity.Player;
import entity.Refreshable;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.Box;

public class Model implements Refreshable{
	private static Model instance;
	private ArrayList<Refreshable> refreshables;
	private ArrayList<GameElement> gameElements;
	private Player currentPlayer;
	private CollisionMatrix collisionMatrix;
	
	//all nodes of the scene. to get their positions and such.
	private ArrayList<Node> gameElementNodes;
	
	//debug, remove this asap
	public static int minCoordDebug = -500;
	public static int maxCoordDebug = 500;
	private Model() {
		refreshables = new ArrayList<Refreshable>();
		//currentPlayer = new Player(Point3D.ZERO);
		collisionMatrix = new CollisionMatrix(maxCoordDebug-minCoordDebug, maxCoordDebug-minCoordDebug);
	}
	private Model(int nombreMobsDebug) {
		
		refreshables = new ArrayList<Refreshable>();
		gameElements = new ArrayList<GameElement>();
		
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
			refreshables.add(newMob);
			gameElements.add(newMob);
		}
		//generating the player
		double x = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
		double z = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
		Point3D pos = new Point3D(x, 0, z);
		currentPlayer = new Player(pos);
		gameElements.add(currentPlayer);
		collisionMatrix = new CollisionMatrix(maxCoordDebug-minCoordDebug,maxCoordDebug-minCoordDebug);
	}
	public static Model getInstance() {
		if(instance == null)
			instance = new Model();
		return instance;
	}
	public static Model newDebugInstance(int nombreMobsDebug) {
		instance = new Model(nombreMobsDebug);
		return instance;
	}
	public Box getFloor() {
		return collisionMatrix.getFloor();
	}
	public Bounds getFloorBounds() {
		return collisionMatrix.getFloorBounds();
	}
	
	/**
	 * this function refreshes all the elements of the object's refreshables ArrayList as well as the currentPlayer object.
	 */
	public void update() {
		for(Refreshable r:refreshables) {
			r.update();
		}
		currentPlayer.update();
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
}
