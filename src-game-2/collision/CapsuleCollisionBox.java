package collision;

import javafx.geometry.Point3D;

public class CapsuleCollisionBox extends CollisionBox {
	
	private double radius;
	private Point3D p1, p2;
	private Point3D anchor;
	
	public CapsuleCollisionBox(String id, Point3D position, double radius, Point3D p1, Point3D p2, Point3D anchor) {
		super(id, position);
		this.radius = radius;
		this.p1 = p1;
		this.p2 = p2;
		tooBigForCollisionDetectionSystem = (this.radius*2 > CollisionGrid.getMapDivisionHeight() || this.radius*2 > CollisionGrid.getMapDivisionWidth() || p1.distance(p2)+this.radius*2 > CollisionGrid.getMapDivisionHeight() || p1.distance(p2)+this.radius*2 > CollisionGrid.getMapDivisionWidth());
		this.anchor = anchor;
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
	protected Point3D getCorrectionSphericalBox(SphericalCollisionBox box) {
		
		return null;
	}

	@Override
	protected boolean collidesCapsuleBox(CapsuleCollisionBox box) {
		return false;
	}
	public Point3D getP1() {
		return p1;
	}
	public Point3D getP2() {
		return p2;
	}
}
