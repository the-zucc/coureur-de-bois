package app;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

import collision.CollisionGrid;
import entity.Entity;
import entity.living.Mob;
import entity.living.human.Player;
import entity.statics.Tree;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import util.Updateable;

public class Model implements Updateable{
	//for singleton implementation
	private static Model instance;
	
	public static Model getInstance() {
		if(instance == null) {
			instance = new Model(80000, 80000, 5, 100, 2000);
		}
		return instance;
	}
	
	//instance variables
	private ArrayList<Entity> entities;
	private Hashtable<String,Entity> gameElementsHashtable;
	private ArrayList<Tree> trees;
	private CollisionGrid collisionGrid;
	private Player currentPlayer;
	private double mapWidth;
	private double mapHeight;
	
	private Model(double mapWidth, double mapHeight, double floorVertexWidth, int mobCount, int treeCount) {
		this.mapHeight = mapHeight;
		this.mapWidth = mapWidth;
		
		//initialize the lists of elements
		entities = new ArrayList<Entity>();
		gameElementsHashtable = new Hashtable<String, Entity>();
		trees = new ArrayList<Tree>();
		
		double gridColumnWidth=100;
		double gridColumnHeight=100;
		
		collisionGrid = CollisionGrid.newInstance(mapWidth, mapHeight, (int)(mapWidth/gridColumnWidth), (int)(mapWidth/gridColumnWidth));
		for(int i = 0; i < mobCount; i++) {
			double x = (double)ThreadLocalRandom.current().nextInt((int)-(mapWidth/2), (int)mapWidth/2);
			double y = (double)ThreadLocalRandom.current().nextInt((int)-(mapWidth/2), (int)mapWidth/2);
			double z = (double)ThreadLocalRandom.current().nextInt((int)-(mapHeight/2), (int)mapHeight/2);
			
			Point3D position = new Point3D(x, y, z);
			Mob newMob = new Mob(position, 100, 100, 1, Color.AQUA);
			
			addElement(newMob);
		}
		for(int i = 0; i < treeCount; i++) {
			double x = (double)ThreadLocalRandom.current().nextInt((int)-(mapWidth/2), (int)mapWidth/2);
			double z = (double)ThreadLocalRandom.current().nextInt((int)-(mapHeight/2), (int)mapHeight/2);
			double y = 0/*CollisionGrid.getInstance().getHeightAt(new Point3D(x, 0, z))*/;
			Point3D position = new Point3D(x, y, z);
			Tree tree = new Tree(position);
			addElement(tree);
		}
		double x = (double)ThreadLocalRandom.current().nextInt((int)-(mapWidth/2), (int)mapWidth/2);
		double z = (double)ThreadLocalRandom.current().nextInt((int)-(mapHeight/2), (int)mapHeight/2);
		Point3D position = new Point3D(0, 0, 0);
		currentPlayer = new Player(position, Color.BLACK);
		addElement(currentPlayer);
	}
	
	public ArrayList<Entity> getGameElements(){
		return entities;
	}
	
	/**
	 * returns the gameElement corresponding to the specified ID.
	 * @param id the id of the element to get
	 * @return the GameElement that has the specified ID.
	 */
	public Entity getEntity(String id) {
		return gameElementsHashtable.get(id);
	}
	
	/**
	 * adds the specified gameElement to the model
	 * @param ge the Element to add to the model
	 */
	public void addElement(Entity ge) {
		if(ge instanceof Tree) {
			trees.add((Tree)ge);
		}else {
			entities.add(ge);
			gameElementsHashtable.put(ge.getId(), ge);
		}
		
	}
	/**
	 * removes the 
	 * @param id
	 */
	public void removeElement(String id) {
		Entity ge = gameElementsHashtable.get(id);
		gameElementsHashtable.remove(id);
		entities.remove(ge);
		
	}
	public double getMapWidth() {
		return mapWidth;
	}
	public double getMapHeight() {
		return mapHeight;
	}
	@Override
	public void update(double deltaTime) {
		for (Entity ge: entities) {
			if(ge instanceof Updateable)
				((Updateable) ge).update(deltaTime);
		}
	}
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	public ArrayList<Tree> getTrees() {
		return trees;
	}
}
