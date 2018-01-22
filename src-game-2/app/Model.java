package app;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

import collision.CollisionGrid;
import entity.GameElement;
import entity.Mob;
import entity.Player;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import util.Updateable;

public class Model implements Updateable{
	//for singleton implementation
	private static Model instance;
	
	public static Model getInstance() {
		if(instance == null) {
			instance = new Model(5000, 5000, 5, 2000, 10);
		}
		return instance;
	}
	
	//instance variables
	private ArrayList<GameElement> gameElements;
	private Hashtable<String,GameElement> gameElementsHashtable;
	private CollisionGrid collisionGrid;
	private Player currentPlayer;
	private double mapWidth;
	private double mapHeight;
	
	private Model(double mapWidth, double mapHeight, double floorVertexWidth, int mobCount, int treeCount) {
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		
		//initialize the lists of elements
		gameElements = new ArrayList<GameElement>();
		gameElementsHashtable = new Hashtable<String, GameElement>();
		
		double gridColumnWidth=100;
		double gridColumnHeight=100;
		
		collisionGrid = CollisionGrid.newInstance(mapWidth, mapHeight, (int)(mapWidth/gridColumnWidth), (int)(mapWidth/gridColumnWidth));
		for(int i = 0; i < mobCount; i++) {
			double x = (double)ThreadLocalRandom.current().nextInt((int)-(mapWidth/2), (int)mapWidth/2);
			double z = (double)ThreadLocalRandom.current().nextInt((int)-(mapHeight/2), (int)mapHeight/2);
			
			Point3D position = new Point3D(x, 0, z);
			Mob newMob = new Mob(position, 100, 100, 1, Color.AQUA);
			
			addElement(newMob);
		}
		double x = (double)ThreadLocalRandom.current().nextInt((int)-(mapWidth/2), (int)mapWidth/2);
		double z = (double)ThreadLocalRandom.current().nextInt((int)-(mapHeight/2), (int)mapHeight/2);
		Point3D position = new Point3D(0, 0, 0);
		currentPlayer = new Player(position, Color.BLACK);
		addElement(currentPlayer);
	}
	
	public ArrayList<GameElement> getGameElements(){
		return gameElements;
	}
	
	/**
	 * returns the gameElement corresponding to the specified ID.
	 * @param id the id of the element to get
	 * @return the GameElement that has the specified ID.
	 */
	public GameElement getElement(String id) {
		return gameElementsHashtable.get(id);
	}
	
	/**
	 * adds the specified gameElement to the model
	 * @param ge the Element to add to the model
	 */
	public void addElement(GameElement ge) {
		gameElements.add(ge);
		gameElementsHashtable.put(ge.getId(), ge);
	}
	public double getMapWidth() {
		return mapWidth;
	}
	public double getMapHeight() {
		return mapHeight;
	}
	@Override
	public void update(double deltaTime) {
		for (GameElement ge: gameElements) {
			if(ge instanceof Updateable)
				((Updateable) ge).update(deltaTime);
		}
	}
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
}
