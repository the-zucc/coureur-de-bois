package model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import app.Main;
import collision.CollisionMatrix;
import entity.DefaultMob;
import entity.GameElement;
import entity.Player;
import entity.Refreshable;
import entity.Tree;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
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
	public static int minCoordDebug = -2500;
	public static int maxCoordDebug = 2500;
	private Model() {
		refreshables = new ArrayList<Refreshable>();
		//currentPlayer = new Player(Point3D.ZERO);
		collisionMatrix = new CollisionMatrix(maxCoordDebug-minCoordDebug, maxCoordDebug-minCoordDebug);
	}
	private Model(int nombreMobsDebug, int nombreArbresDebug) {
		collisionMatrix = new CollisionMatrix(maxCoordDebug-minCoordDebug,maxCoordDebug-minCoordDebug);
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
		//loop for generating multiple tree objects, for debug purposes
		for(int i = 0; i < nombreArbresDebug; i++) {
			double x = (double)ThreadLocalRandom.current().nextInt(min, max);
			double z = (double)ThreadLocalRandom.current().nextInt(min, max);
			Point3D pos = new Point3D(x, 0, z);
			Tree newTree = new Tree(pos);
			gameElements.add(newTree);
		}
		
		//generating the player
		double x = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
		double z = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
		Point3D pos = new Point3D(x, 0, z);
		currentPlayer = new Player(pos);
		gameElements.add(currentPlayer);
		
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
		for(Refreshable r:refreshables) {
			r.update(deltaTime);
		}
		currentPlayer.update(deltaTime);
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
