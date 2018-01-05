package model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import entity.DefaultMob;
import entity.Player;
import entity.Refreshable;
import javafx.geometry.Point3D;

public class Model implements Refreshable{
	private static Model instance;
	private ArrayList<Refreshable> refreshables;
	private Player currentPlayer;
	private FloorMatrix floorMatrix;
	private Model() {
		refreshables = new ArrayList<Refreshable>();
		currentPlayer = new Player(Point3D.ZERO);
		floorMatrix = new FloorMatrix(1000, 1000);
	}
	private Model(int nombreMobsDebug) {
		refreshables = new ArrayList<Refreshable>();
		int min = -500;
		int max = 500;
		for(int i = 0; i < nombreMobsDebug; i++) {
			double x = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			double z = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			Point3D pos = new Point3D(x, 0, z);
			DefaultMob newMob = new DefaultMob(pos);
			double xTarget = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			double zTarget = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			Point3D target = new Point3D(xTarget, 0, zTarget);
			newMob.targetPoint(target);
			refreshables.add(newMob);
		}
		double x = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
		double z = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
		Point3D pos = new Point3D(x, 0, z);
		currentPlayer = new Player(pos);
		floorMatrix = new FloorMatrix(1000, 1000);
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
	
	/**
	 * this function refreshes all the elements of the object's refreshables ArrayList as well as the currentPlayer object.
	 */
	public void refresh() {
		for(Refreshable r:refreshables) {
			r.refresh();
		}
		currentPlayer.refresh();
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public FloorMatrix getFloorMatrix(){
		return floorMatrix;
	}
}
