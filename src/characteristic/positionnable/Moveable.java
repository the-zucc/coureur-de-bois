package characteristic.positionnable;

import javafx.geometry.Point3D;

public interface Moveable extends Positionnable{
	public void moveTo(Point3D nextPosition);
	public Point3D computeNextPosition(double secondsPassed);
}
