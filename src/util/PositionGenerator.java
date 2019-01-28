package util;

import java.util.ArrayList;

import characteristic.positionnable.Positionnable2D;
import entity.statics.village.Tipi;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import village.Village;

public class PositionGenerator {
	
	public static Point2D convert2D(Point3D pos){
		return new Point2D(pos.getX(), pos.getZ());
	}
	public static Point3D generateRandom3DPosition(Map map, double minY, double maxY){
		double mapWidth = map.getMapWidth();
		double mapHeight = map.getMapHeight();
		double x = Math.random()*mapWidth-mapWidth/2;
		double z = Math.random()*mapHeight-mapHeight/2;
		double y = Math.random()*(maxY-minY)+minY;
		return new Point3D(x,y,z);
	}
	public static Point3D generateRandom3DPositionOnFloor(Map map){
		Point2D pos;
		do {
			double mapWidth = map.getMapWidth();
			double mapHeight = map.getMapHeight();
			double x = Math.random() * mapWidth - mapWidth / 2;
			double z = Math.random() * mapHeight - mapHeight / 2;
			pos = new Point2D(x, z);
		}while(map.getWaterLevel() <= map.getHeightAt(pos));

		Point3D returnVal = new Point3D(pos.getX(),map.getHeightAt(pos),pos.getY());
		return returnVal;
	}
	public static Point2D generate2DPositionInRadius(Point2D center, double radius){
		double norme = Math.random()*radius;
		double angle = Math.toRadians(Math.random()*360);
		double x = Math.cos(angle)*norme;
		double y = Math.sin(angle)*norme;
		return new Point2D(x,y).add(center);
	}
	public static Point3D getFloorPosition(Point2D xz, Map map){
		return new Point3D(xz.getX(), map.getHeightAt(xz), xz.getY());
	}
	public static Point2D generate2DPositionNotInVillages(Map map, ArrayList<Village> villages) throws Exception{
		Point2D pos;
		int inVillageCount = 0;//counter, incremented each time the program tries to generate a position,
		// so that if no position is found after a certain number of tries, it restarts throws an exception (to generate a new map)
		boolean inVillage = false;
		do{
			inVillage = false;
			pos = convert2D(generateRandom3DPositionOnFloor(map));
			for (Village v :
					villages) {
				if(v.get2DPosition().distance(pos) < v.getRadius())
					inVillage = true;
			}
			inVillageCount++;
			if(inVillageCount > 10000){
				throw new Exception("Had to recreate a map, was too small for the number of villages.");
			}
		}while(inVillage);
		return pos;
	}
	public static Point2D generateTipiPositionInVillage(Village v){
		Point2D pos;
		boolean positionInvalid = false;
		do {
			positionInvalid = false;
			pos = generate2DPositionInRadius(v.get2DPosition(), v.getRadius());
			for (Tipi t :
					v.getTipis()) {
				if(t.get2DPosition().distance(pos) < t.getRadius()){
					positionInvalid = true;
				}
			}
		} while(positionInvalid);
		return pos;
	}

}
