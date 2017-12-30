package entity;

import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class SampleCube {
	private Box box;
	private double x, y, z;
	public SampleCube(double largeur, double hauteur, double profondeur, double x, double y, double z){
		box = new Box(largeur, hauteur, profondeur);
		box.setTranslateX(x);
		box.setTranslateY(y);
		box.setTranslateZ(z);
		box.setRotationAxis(Rotate.Y_AXIS);
		box.setRotate(45);
		box.setRotationAxis(Rotate.X_AXIS);
		box.setRotate(45);
		//box.setRotationAxis(Rotate.Z_AXIS);
		//box.setRotate(45);
		this.x = x;
		this.y = y;
		this.z = z;
		
	}
	public Box getBox(){
		return box;
	}
}
