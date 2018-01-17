package app;

import java.util.ArrayList;
import java.util.Hashtable;

import entity.GameElement;
import util.Updateable;

public class Model implements Updateable{
	//for singleton implementation
	private static Model instance;
	
	public static Model getInstance() {
		if(instance == null) {
			instance = new Model(5000, 5000, 5, 200, 10);
		}
		return instance;
	}
	
	//instance variables
	private ArrayList<GameElement> gameElements;
	private Hashtable<String,GameElement> gameElementsHashtable;
	private CollisionGrid collisionGrid;
	
	private Model(double mapWidth, double mapHeight, double floorVertexWidth, int mobCount, int treeCount) {
		double gridColumnWidth=100;
		double gridColumnHeight=100;
		collisionGrid = new CollisionGrid(mapWidth, mapHeight, (int)(mapWidth/gridColumnWidth), (int)(mapWidth/gridColumnWidth));
		
	}
	public ArrayList<GameElement> getGameElements(){
		return gameElements;
	}
	public void addElement(GameElement ge) {
		gameElements.add(ge);
		gameElementsHashtable.put(ge.getId(), ge);
	}
	@Override
	public void update(double deltaTime) {
		for (GameElement ge: gameElements) {
			if(ge instanceof Updateable)
				((Updateable) ge).update(deltaTime);
		}
	}
}
