package entity;

import java.util.Hashtable;

import characteristic.positionnable.Collideable;
import characteristic.positionnable.GravityAffected;
import game.GameLogic;
import javafx.geometry.Point3D;
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
	public void updateGravityVector() {
		gravity = gravity.add(GameLogic.getGravity());
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
	public Point3D computeNextPosition(){
		updateGravityVector();
		Point3D next = super.computeNextPosition();
		if(next != null){
			return next.add(getGravityVector());
		}
		return null;
	}

}
