package model;

import javafx.geometry.Point3D;

public class Engine {
	//gravity force is positive, as Y goes from up to down
	private static Point3D globalGravityVector = new Point3D(0,2,0);
	private static Point3D globalJumpVector = new Point3D(0,-30, 0);
	
	public static Point3D getGlobalGravityVector() {
		return globalGravityVector;
	}
	public static Point3D getGlobalJumpVector() {
		return globalJumpVector;
	}
	/**
	 * this function should be called 60 times per second. It is responsible for updating the game objects and for drawing them on screen
	 */
	public static void MainLoop() {
		Model.getInstance().update();
	}
}
