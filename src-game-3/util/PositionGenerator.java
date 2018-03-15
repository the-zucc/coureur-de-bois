package util;

import java.util.ArrayList;

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
		double mapWidth = map.getMapWidth();
		double mapHeight = map.getMapHeight();
		double x = Math.random()*mapWidth-mapWidth/2;
		double z = Math.random()*mapHeight-mapHeight/2;
		Point2D pos = new Point2D(x,z);
		Point3D returnVal = new Point3D(x,map.getHeightAt(pos),z);
		return returnVal;
	}
	public static Point2D generate2DPositionInRadius(Point2D center, double radius){
		return null;
	}
	public static Point3D generate3DPositionNotInVillages(Map map, ArrayList<Village> villages){
		Point3D pos;
		do{
			pos = generateRandom3DPositionOnFloor(map);
			break;
		}while(true);
		Point2D converted = convert2D(pos);
		for(Village v:villages){
			
		}
		return null;
	}
	
}
