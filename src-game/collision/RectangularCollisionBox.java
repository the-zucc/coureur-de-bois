package collision;

import javafx.geometry.Point3D;

public class RectangularCollisionBox extends CollisionBox {
	public RectangularCollisionBox(Point3D position) {
		super(position);
	}

	@Override
	protected boolean collidesCircularBox(CircularCollisionBox box) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean collidesRectangularBox(RectangularCollisionBox box) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean collidesDiagonalBox(DiagonalCollisionBox box) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Point3D getCorrectionCircularBox(CircularCollisionBox box) {
		// TODO Auto-generated method stub
		return new Point3D(0,0,0);
	}

	@Override
	protected Point3D getCorrectionRectangularBox(RectangularCollisionBox box) {
		// TODO Auto-generated method stub
		return new Point3D(0,0,0);
	}

	@Override
	protected Point3D getCorrectionDiagonalBox(DiagonalCollisionBox box) {
		// TODO Auto-generated method stub
		return new Point3D(0,0,0);
	}
}
