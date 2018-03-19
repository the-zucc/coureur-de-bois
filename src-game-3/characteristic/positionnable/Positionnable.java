package characteristic.positionnable;

import javafx.geometry.Point3D;

public interface Positionnable extends Positionnable2D{
	public void setPosition(Point3D position);
	public Point3D getPosition();
}
