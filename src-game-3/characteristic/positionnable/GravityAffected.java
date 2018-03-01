package characteristic.positionnable;

import javafx.geometry.Point3D;

public interface GravityAffected extends Moveable {
	public Point3D getGravityVector();
	public void updateGravityVector();
	public void resetGravityVector();
}
