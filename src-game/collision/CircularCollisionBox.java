package collision;

import javafx.geometry.Point3D;
import model.CollisionMatrix;
import model.Model;

public class CircularCollisionBox extends CollisionBox {
	
	private double radius;
	
	public CircularCollisionBox(Point3D position, double radius) {
		super(position);
		this.radius=radius;
		if(radius > CollisionMatrix.getMapDivisionHeight()/2 || radius > CollisionMatrix.getMapDivisionWidth()/2) {
			tooBigForCollisionDetectionSystem = true;
		}
		else
			tooBigForCollisionDetectionSystem = false;
		setPosition(position);
	}
	
	public double getRadius() {
		return radius;
	}
	
	@Override
	protected boolean collidesCircularBox(CircularCollisionBox box) {
		//inline function to get the most performance out of it
		return Math.hypot(box.getPosition().getX()-position.getX(), box.getPosition().getZ()-position.getZ()) < (box.getRadius()+radius);
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
		double distance = Math.hypot(box.getPosition().getX()-position.getX(), box.getPosition().getZ()-position.getZ());
		double magnitude = distance - (box.getRadius()+radius);
		double frac = magnitude/distance;
		return new Point3D(frac*(box.getPosition().getX()-position.getX()), 0, frac*(box.getPosition().getZ()-position.getZ()));
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
	
	
	

	
}