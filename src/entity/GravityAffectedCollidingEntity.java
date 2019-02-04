package entity;

import characteristic.Messenger;
import characteristic.positionnable.GravityAffected;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import util.PositionGenerator;

public abstract class GravityAffectedCollidingEntity extends MovingCollidingEntity implements GravityAffected{
	private Point3D gravity;
	protected boolean shouldFall() {
		return true;
	}
	public GravityAffectedCollidingEntity(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		gravity = new Point3D(0,0,0);
	}

	@Override
	public Point3D getGravity() {
		return gravity;
	}

	@Override
	/**
	 * updates the gravity vector to the new value with the applied gravity acceleration.
	 */
	public void updateGravityVector(double secondsPassed) {
		if(shouldFall()) {
			if(position.getY() < Map.getMainMap().getHeightAt(position2D)){
				gravity = gravity.add(GameLogic.getGravity().multiply(secondsPassed));
			}			
		}
	}
	@Override
	public void addForceToGravity(Point3D force){
		if(force != null)
			this.gravity = this.gravity.add(force);
	}
	@Override
	/**
	 * resets the gravity vector to 0.
	 */
	public void resetGravityVector() {
		gravity = Point3D.ZERO;
	}
	
	@Override
	/**
	 * computes the next position. uses the superclass' method, but adds gravity to the returned value.
	 */
	public Point3D computeNextPosition(float secondsPassed){
		updateGravityVector(secondsPassed);
		Point3D next = super.computeNextPosition(secondsPassed);
		if(next != null){
			if(shouldFall()) {
				next =  next.add(getGravity().multiply(secondsPassed));				
			}
		}
		float floorHeight = this.map.getHeightAt(PositionGenerator.convert2D(next));
		if(next.getY() > floorHeight){
			next = new Point3D(next.getX(), floorHeight, next.getZ());
			resetGravityVector();
		}
		if(next != null){
			return next;
		}
		return position;
	}
	@Override
	public void update(float secondsPassed){
		super.update(secondsPassed);
	}
}
