package collision;

import characteristic.positionnable.Collideable;
import game.Map;
import javafx.geometry.Point3D;

public class SphericalCollisionBox extends CollisionBox{

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
	public Point3D getCorrectionSphericalBox(SphericalCollisionBox box) {
		Point3D position = getPosition();

		double distance = Math.hypot(box.getPosition().getX()-position.getX(), box.getPosition().getZ()-position.getZ());
		double magnitude = distance - (box.getRadius()+radius);
		double frac = magnitude/distance;
		return new Point3D(frac*(box.getPosition().getX()-position.getX()), frac*(box.getPosition().getY()-position.getY()), frac*(box.getPosition().getZ()-position.getZ()));
	}

	@Override
	public boolean collidesSphericalBox(SphericalCollisionBox b1) {
		Point3D pos = b1.getPosition();
		Point3D thisPos = getPosition();
		return pos.distance(thisPos) < (b1.getRadius()+radius);
	}

	@Override
	public boolean movesOnCollision() {
		return getCollideable().canMoveOnCollision();
	}

	@Override
	public double computeCollidingWeight() {
		return getCollideable().computeCollidingWeight();
	}

	@Override
	protected boolean isTooBigForCollisionOptimization() {
		return this.radius*2 > Map.getCollisionMapDivisionWidth() || this.radius*2 > Map.getCollisionMapDivisionHeight();
	}
	@Override
	public void update() {
		updatePosition();
	}
}
