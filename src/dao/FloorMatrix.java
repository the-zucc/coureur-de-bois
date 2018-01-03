package dao;

import java.util.Vector;

public class FloorMatrix {
	private static FloorMatrix instance;
	Vector<Vector<Integer>> floorHeightMatrix;
	private FloorMatrix() {
		floorHeightMatrix = new Vector<Vector<Integer>>();
	}
	public static FloorMatrix getInstance() {
		if(instance == null)
			instance = new FloorMatrix();
		return instance;
	}
	public double getHeighAt(double x, double z) {
		//TODO replace this, simply debug
		return 0;
	}
}
