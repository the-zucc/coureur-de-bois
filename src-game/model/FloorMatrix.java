package model;

import java.util.Vector;

import javafx.geometry.Point3D;

public class FloorMatrix {
	private Vector<Vector<Double>> heightMatrix;
	
	public FloorMatrix(int length, int height){
		heightMatrix = new Vector<Vector<Double>>(height);
		for(int i = 0; i < height; i++){
			heightMatrix.set(i, new Vector<Double>(length));
			for(int j = 0; j < length; j++){
				heightMatrix.get(i).set(j, 0.0);
			}
		}
	}
	public double getHeightAt(Point3D position){
		return heightMatrix.get((int)position.getX()).get((int)position.getZ());
	}
}
