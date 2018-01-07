package model;

import java.util.ArrayList;
import java.util.Vector;

import entity.GameElement;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.shape.Box;

public class FloorMatrix {
	
	private Box floor;
	private Vector<Vector<Double>> heightMatrix;
	
	private ArrayList<GameElement> elementsSortedByX;
	private ArrayList<GameElement> elementsSortedByZ;
	private ArrayList<GameElement> elementsSortedByY;
	
	
	public FloorMatrix(int length, int height){
		//heightMatrix = new Vector<Vector<Double>>();
		floor = new Box(length, 1, height);
//		for(int i = 0; i < height; i++){
//			heightMatrix.add(new Vector<Double>());
//			for(int j = 0; j < length; j++){
//				heightMatrix.get(i).add(0.0);
//			}
//		}
	}
	public double getHeightAt(Point3D position){
		return 0;
		//return heightMatrix.get((int)position.getZ()).get((int)position.getX());
	}
	public Box getFloor() {
		return floor;
	}
	public Bounds getFloorBounds() {
		return floor.getBoundsInLocal();
	}
	public void CheckCollisions(GameElement ge) {
		
	}
}
