package model;

import java.util.Vector;

import javafx.geometry.Point3D;

public class FloorMatrix {
	private Vector<Vector<Double>> heightMatrix;
	
	public FloorMatrix(int length, int height){
		heightMatrix = new Vector<Vector<Double>>();
		for(int i = 0; i < height; i++){
			heightMatrix.add(new Vector<Double>());
			for(int j = 0; j < length; j++){
				heightMatrix.get(i).add(0.0);
			}
		}
	}
	public double getHeightAt(Point3D position){
		return 0;
		//return heightMatrix.get((int)position.getZ()).get((int)position.getX());
	}
}
