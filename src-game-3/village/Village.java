package village;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import characteristic.positionnable.*;
import entity.living.human.Villager;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import util.PositionGenerator;

public class Village implements Positionnable2D{
	private Point3D position;
	private ArrayList<Villager> villagers;
	private Point2D position2D;
	
	public Village(Point3D position, int tipiCount, double radius, int tipiRowCount, int villagerCount) {

		int tipiCountPerRow = tipiCount;
		if(tipiCount > 6)
			tipiCountPerRow=6;
		double angleBetweenTipis = Math.toRadians(360/tipiCountPerRow);

		setPosition(position);
		villagers = new ArrayList<Villager>();
		/*
		for (int i = 0; i < villagerCount; i++) {
			double x, y, z;
			Villager v;
			do{
				v = new Villager(PositionGenerator.generateRandom3DPositionOnFloor(Map.getInstance()));
			}while(v.distanceFrom(this) > this.radius);
			villagers.add(v)
		}
		*/
	}
	
	@Override
	public void setPosition(Point3D position) {
		this.position = position;
		set2DPosition(compute2DPosition(position));
	}

	@Override
	public Point3D getPosition() {
		return position;
	}
	public void addEntitiesToMap(Map m) {
		
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
	public double distanceFrom(Positionnable2D arg0) {
		return get2DPosition().distance(arg0.get2DPosition());
	}
}
