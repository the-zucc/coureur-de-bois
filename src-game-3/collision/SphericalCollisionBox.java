package collision;

import characteristic.positionnable.Collideable;
import game.Map;
import javafx.geometry.Point3D;

public class SphericalCollisionBox extends CollisionBox {

	private double radius;
	public double getRadius(){
		return radius;
	}
	private Point3D relativePosition;
	private Point3D absolutePosition;
	public Point3D getPosition(){
		return absolutePosition;
	}
	public Point3D computePosition(){
		return getCollideable().getPosition().add(relativePosition);
	}
	public void updatePosition(){
		this.absolutePosition = computePosition();
	};

	public SphericalCollisionBox(double radius, Collideable collideable, Point3D relativePosition, Map map){
		super(collideable, map);
		this.radius = radius;
		this.relativePosition = relativePosition;
	}

	@Override
	public Point3D getCorrectionCapsuleBox(CapsuleCollisionBox b1) {
		return null;
	}

	@Override
	public boolean collidesCapsuleBox(CapsuleCollisionBox b1) {
		return false;
	}

	@Override
	public Point3D getCorrectionSphericalBox(SphericalCollisionBox b1) {
		Point3D pos = b1.getPosition();
		Point3D thisPos = getPosition();

		double distance = pos.distance(thisPos);
		double magnitude = distance - (b1.getRadius()+radius);
		double frac = magnitude/distance;
		return new Point3D(frac*(pos.getX()-thisPos.getX()), frac*(pos.getY()-thisPos.getY()), frac*(pos.getZ()-thisPos.getZ()));
	}

	@Override
	public boolean collidesSphericalBox(SphericalCollisionBox b1) {
		Point3D pos = b1.getPosition();
		Point3D thisPos = getPosition();
		return Math.hypot(Math.hypot(pos.getX()-thisPos.getX(), b1.getPosition().getZ()-thisPos.getZ()),b1.getPosition().getY()-thisPos.getY()) < (b1.getRadius()+radius);
	}

	@Override
	public boolean movesOnCollision() {
		return true;
	}

	@Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	protected boolean isTooBigForCollisionOptimization() {
		return this.radius*2 > Map.getCollisionMapDivisionWidth() || this.radius*2 > Map.getCollisionMapDivisionHeight();
	}
	public void correct(){

	}
}
