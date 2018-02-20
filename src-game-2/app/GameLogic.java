package app;

import javafx.geometry.Point3D;

public class GameLogic {
	private static Point3D gravity = new Point3D(0,1,0);
	private static Point3D jump = new Point3D(0,-20,0);
	
	public static Point3D getGravity(){
		return gravity;
	}
	
	public static Point3D getJump(){
		return jump;
	}
}
