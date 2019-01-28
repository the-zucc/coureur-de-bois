package village;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import characteristic.positionnable.*;
import entity.living.human.Villager;
import entity.statics.village.Tipi;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import util.EntityFactory;
import util.PositionGenerator;

public class Village implements Positionnable2D{
	private Point3D position;
	private ArrayList<Villager> villagers;
	private ArrayList<Tipi> tipis;
	public ArrayList<Tipi> getTipis() {
		return tipis;
	}

	private Point2D position2D;
	private double radius;
	
	public Village(Point2D position2D, int tipiCount, double radius, int villagerCount, Map m) {
		this.radius = radius;
		this.position2D = position2D;
		villagers = new ArrayList<Villager>();
		tipis = new ArrayList<Tipi>();

		for (int i = 0; i < villagerCount; i++) {
			double x, y, z;
			Villager v = EntityFactory.spawnVillagerFromVillage(this, m);
			villagers.add(v);
		}
		for (int i = 0; i < tipiCount; i++) {
			Tipi t = EntityFactory.buildTipiAroundVillage(this, m);
			tipis.add(t);
		}

	}
	
	public void addEntitiesToMap(Map m) {
		for (Villager v :villagers) {
			m.addEntity(v);
		}
		for (Tipi t:tipis) {
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
	public double distance2DFrom(Positionnable2D arg0) {
		return get2DPosition().distance(arg0.get2DPosition());
	}

	public double getRadius() {
		return radius;
	}
}
