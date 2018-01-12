package collision;

import javafx.geometry.Point3D;

public class CylindricalCollisionBox extends CollisionBox {
	private double radius;
	
	public CylindricalCollisionBox(Point3D position, double radius) {
		super(position);
		this.radius = radius; 
	}
	
	public double getRadius() {
		return radius;
	}
	
	@Override
	protected boolean collidesSphericalBox(SphericalCollisionBox box) {
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
	protected Point3D getCorrectionSphericalBox(SphericalCollisionBox box) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Point3D getCorrectionRectangularBox(RectangularCollisionBox box) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Point3D getCorrectionDiagonalBox(DiagonalCollisionBox box) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean collidesCylindricalBox(CylindricalCollisionBox box) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Point3D getCorrectionCylindricalBox(CylindricalCollisionBox arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
