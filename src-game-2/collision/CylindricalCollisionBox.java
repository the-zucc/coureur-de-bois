package collision;

import javafx.geometry.Point3D;

public class CylindricalCollisionBox extends CollisionBox {
	
	private double radius;
	private double height;
	
	public CylindricalCollisionBox(String id, Point3D position, double radius, double height){
		super(id, position);
		this.radius = radius;
		this.height = height;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public double getHeight() {
		return height;
	}
	
	@Override
	protected boolean collidesSphericalBox(SphericalCollisionBox box) {
		Point3D boxPosition = box.getPosition();
		double boxRadius = box.getRadius();
		
		double currentBoxUpperBound = position.getY() + height/2;
		double currentBoxLowerBound = position.getY() - height/2;
		if(boxPosition.getY() > currentBoxUpperBound && boxPosition.getY() < currentBoxLowerBound)
			if(Math.hypot(boxPosition.getX()-position.getX(), boxPosition.getZ()-position.getZ())<radius+box.getRadius())
				return true;
		if(boxPosition.getY()+boxRadius > currentBoxUpperBound && boxPosition.getY()+boxRadius < currentBoxLowerBound) 
			if(Math.hypot(boxPosition.getX()-position.getX(), boxPosition.getZ()-position.getZ())<radius)
				return true;
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
