package collision;

import javafx.geometry.Point3D;

public class DiagonalCollisionBox extends CollisionBox {
	public DiagonalCollisionBox(Point3D position) {
		super(position);
	}

	@Override
	protected boolean collidesCircularBox(SphericalCollisionBox box) {
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
	protected Point3D getCorrectionCircularBox(SphericalCollisionBox box) {
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