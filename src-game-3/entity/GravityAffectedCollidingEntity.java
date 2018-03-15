package entity;

import java.util.Hashtable;

import characteristic.positionnable.Collideable;
import characteristic.positionnable.GravityAffected;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import util.PositionGenerator;
import visual.Component;

public abstract class GravityAffectedCollidingEntity extends MovingCollidingEntity implements GravityAffected{
	private Point3D gravity;

	public GravityAffectedCollidingEntity(Point3D position) {
		super(position);
		gravity = new Point3D(0,0,0);
	}

	@Override
	public Point3D getGravityVector() {
		return gravity;
	}

	@Override
	/**
	 * updates the gravity vector to the new value with the applied gravity acceleration.
	 */
	public void updateGravityVector(double secondsPassed) {
		if(position.getY() < Map.getInstance().getHeightAt(position2D)){
			gravity = gravity.add(GameLogic.getGravity().multiply(secondsPassed));
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
	public Point3D computeNextPosition(double secondsPassed){
		updateGravityVector(secondsPassed);
		Point3D next = super.computeNextPosition(secondsPassed);
		if(next != null){
			next =  next.add(getGravityVector().multiply(secondsPassed));
		}
		double floorHeight=Map.getInstance().getHeightAt(PositionGenerator.convert2D(next));
		if(next.getY() > floorHeight){
			next = new Point3D(next.getX(), floorHeight, next.getZ());
			resetGravityVector();
		}
		//System.out.println("nextpos:"+next);
		//System.out.println("grav:"+getGravityVector());
		if(next != null){
			return next;
		}
		return position;
	}
}
