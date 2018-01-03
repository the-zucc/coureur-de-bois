package model;

import java.util.ArrayList;

import entity.Player;
import entity.Refreshable;

public class Model implements Refreshable{
	private static Model instance;
	private ArrayList<Refreshable> gameElements;
	private Player currentPlayer;
	private Model() {
		gameElements = new ArrayList<Refreshable>();
		currentPlayer = new Player();
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
}
