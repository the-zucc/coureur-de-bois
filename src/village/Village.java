package village;

import java.util.ArrayList;

import characteristic.positionnable.*;
import entity.living.human.Villager;
import entity.statics.village.House;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import util.EntityFactory;

public class Village implements Positionnable2D{
	private Point3D position;
	private ArrayList<Villager> villagers;
	private ArrayList<House> houses;
	public ArrayList<House> getHouses() {
		return houses;
	}

	private Point2D position2D;
	private float radius;
	
	public Village(Point2D position2D, int tipiCount, float radius, int villagerCount, Map m) {
		this.radius = radius;
		this.position2D = position2D;
		villagers = new ArrayList<Villager>();
		houses = new ArrayList<House>();

		for (int i = 0; i < villagerCount; i++) {
			double x, y, z;
			Villager v = EntityFactory.spawnVillagerFromVillage(this, m);
			villagers.add(v);
		}
		for (int i = 0; i < tipiCount; i++) {
			House t = EntityFactory.buildTipiAroundVillage(this, m);
			houses.add(t);
		}

	}
	
	public void addEntitiesToMap(Map m) {
		for (Villager v :villagers) {
			m.addEntity(v);
		}
		for (House t: houses) {
			m.addEntity(t);
		}
	}

	@Override
	public Point2D compute2DPosition(Point3D position3d) {
		return new Point2D(position3d.getX(), position3d.getZ());
	}

	@Override
	public void set2DPosition(Point2D position2d) {
		this.position2D = position2d;
		
	}

	@Override
	public Point2D get2DPosition() {
		return position2D;
	}

	@Override
	public float distance2DFrom(Positionnable2D arg0) {
		return (float)get2DPosition().distance(arg0.get2DPosition());
	}

	public float getRadius() {
		return radius;
	}
}
