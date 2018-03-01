package game;

import java.util.ArrayList;
import java.util.Hashtable;

import characteristic.ComponentOwner;
import characteristic.Messageable;
import characteristic.Updateable;
import characteristic.positionnable.Collideable;
import entity.Entity;
import javafx.geometry.Point3D;
import visual.Component;

public class Map implements ComponentOwner, Updateable, Messageable{
	private static long updateCount = 0;
	
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
		if(updateCount % 10 == 0){//to be executed once every 10 ticks
			for(Updateable u:updateables){
				if(u instanceof ComponentOwner){
					if(!((ComponentOwner) u).isComponentInScene())
						((ComponentOwner) u).placeComponentInScene();
				}
			}
		}
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}
	
	/**
	 * COLLISIONS SECTION
	 */
	//static variables
	private static int collisionMapDivisionWidth=100;
	private static int collisionMapDivisionHeight=collisionMapDivisionWidth;
	public double getHeightAt(Point3D position){
		return 0;
	}

	@Override
	public void onMessageReceived(Hashtable<String, ? extends Object> message) {
		if(message.containsKey("start")){
			Hashtable<String, Double> params = (Hashtable<String, Double>)message.get("start");
		}
	}
}
