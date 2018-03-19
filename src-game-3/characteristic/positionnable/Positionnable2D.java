package characteristic.positionnable;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

public interface Positionnable2D{
	public Point2D compute2DPosition(Point3D position3D);
	public void set2DPosition(Point2D position2D);
	public Point2D get2DPosition();
	public double distanceFrom(Positionnable2D arg0);
}
