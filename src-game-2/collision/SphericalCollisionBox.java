package collision;

import javafx.geometry.Point3D;
import app.Model;

public class SphericalCollisionBox extends CollisionBox {
	
	private double radius;
	
	public SphericalCollisionBox(Point3D position, double radius) {
		super(position);
		this.radius=radius;
		if(radius > CollisionGrid.getMapDivisionHeight()/2 || radius > CollisionGrid.getMapDivisionWidth()/2) {
			tooBigForCollisionDetectionSystem = true;
		}
		else
			tooBigForCollisionDetectionSystem = false;
		//setPosition(position);
	}
	
	public double getRadius() {
		return radius;
	}
	
	@Override
	protected boolean collidesSphericalBox(SphericalCollisionBox box) {
		//inline function to get the most performance out of it
		return Math.hypot(Math.hypot(box.getPosition().getX()-position.getX(), box.getPosition().getZ()-position.getZ()),box.getPosition().getY()-position.getY()) < (box.getRadius()+radius);
	}

	@Override
	protected Point3D getCorrectionSphericalBox(SphericalCollisionBox box) {
		// TODO Auto-generated method stub
		double distance = Math.hypot(box.getPosition().getX()-position.getX(), box.getPosition().getZ()-position.getZ());
		double magnitude = distance - (box.getRadius()+radius);
		double frac = magnitude/distance;
		return new Point3D(frac*(box.getPosition().getX()-position.getX()), frac*(box.getPosition().getY()-position.getY()), frac*(box.getPosition().getZ()-position.getZ()));
	}
	
	@Override
	protected boolean collidesRectangularBox(RectangularCollisionBox box) {
		return false;
	}
	
	@Override
	protected Point3D getCorrectionRectangularBox(RectangularCollisionBox box) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected boolean collidesDiagonalBox(DiagonalCollisionBox box) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	protected Point3D getCorrectionDiagonalBox(DiagonalCollisionBox box) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean collidesCylindricalBox(CylindricalCollisionBox box) {
		double boxRadius = box.getRadius();
		
		double currentBoxUpperBound = box.getPosition().getY() + box.getHeight()/2;
		double currentBoxLowerBound = box.getPosition().getY() - box.getHeight()/2;
		if(position.getY() > currentBoxUpperBound && position.getY() < currentBoxLowerBound)
			if(Math.hypot(position.getX()-position.getX(), position.getZ()-position.getZ())<radius+box.getRadius())
				return true;
		if(position.getY()+boxRadius > currentBoxUpperBound && position.getY()+boxRadius < currentBoxLowerBound) 
			if(Math.hypot(position.getX()-position.getX(), position.getZ()-position.getZ())<box.getRadius())
				return true;
		return false;
	}

	@Override
	protected Point3D getCorrectionCylindricalBox(CylindricalCollisionBox arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

	
}