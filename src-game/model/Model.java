package model;

import java.util.ArrayList;

import entity.Player;
import entity.Refreshable;

public class Model implements Refreshable{
	private static Model instance;
	private ArrayList<Refreshable> gameElements;
	private Player currentPlayer;
	private FloorMatrix floorMatrix;
	private Model() {
		gameElements = new ArrayList<Refreshable>();
		currentPlayer = new Player();
		floorMatrix = new FloorMatrix(1000, 1000);
	}
	public static Model getInstance() {
		if(instance == null)
			instance = new Model();
		return instance;
	}
	
	public void refresh() {
		for(Refreshable r:gameElements) {
			r.refresh();
		}
	}
	public FloorMatrix getFloorMatrix(){
		return floorMatrix;
	}
}
