package game;

import java.util.ArrayList;

import characteristic.ComponentOwner;
import characteristic.Updateable;
import characteristic.positionnable.Collideable;
import entity.Entity;
import javafx.geometry.Point3D;
import visual.Component;

public class Map implements ComponentOwner, Updateable{
	
	private static Map instance;
	
	public static Map getInstance(){
		if(instance == null)
			instance = new Map();
		return instance;
	}
	
	private ArrayList<Collideable> collideables;
	private ArrayList<Updateable> updateables;
	private ArrayList<Entity> entities;
	
	private Point3D position;
	private Component component;
	
	private float[][] floorVertices;
	
	public Map(){
		collideables = new ArrayList<Collideable>();
		updateables = new ArrayList<Updateable>();
		entities = new ArrayList<Entity>();
		setPosition(new Point3D(0,0,0));
		component = buildComponent();
	}
	
	public double getFloorYAt(double x, double z){
		return 0;
	}

	@Override
	public void setPosition(Point3D position) {
		this.position = position;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}
	@Override
	/**
	 * 
	 */
	public Component buildComponent() {
		Component returnVal = new Component("map");
		return null;
	}

	@Override
	public Component getComponent() {
		return component;
	}

	@Override
	public boolean isComponentInScene() {
		
		return getComponent().getParent() != null;
	}

	@Override
	public void placeComponentInScene() {
		
	}
	public void addEntity(Entity e){
		if(e instanceof Updateable){
			updateables.add(((Updateable)e));
		}
		if(e instanceof Collideable){
			collideables.add((Collideable)e);
		}
		entities.add(e);
	}

	@Override
	public void update(double secondsPassed) {
		for(Updateable u:updateables){
			u.update(secondsPassed);
		}
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}
}
