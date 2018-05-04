package collision;

import javafx.geometry.Point3D;

public class SphericalCollisionBox extends CollisionBox {
	
	private double radius;
	
	public SphericalCollisionBox(String id, Point3D position, double radius) {
		super(id, position);
		this.radius=radius;
		if(radius*2 > CollisionGrid.getMapDivisionHeight() || radius*2 > CollisionGrid.getMapDivisionWidth()) {
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
	protected boolean collidesCapsuleBox(CapsuleCollisionBox box) {
		
		return false;
	}
}