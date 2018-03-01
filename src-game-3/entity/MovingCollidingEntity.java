package entity;

import java.util.Hashtable;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class MovingCollidingEntity extends VisibleCollidingEntity{

	private Point3D movement;
	
	public MovingCollidingEntity(Point3D position) {
		super(position);
	}

	@Override
	public abstract Component buildComponent();

	@Override
	public boolean shouldUpdate() {
		return true;
	}

	@Override
	public Point3D computeNextPosition() {
		return getPosition().add(movement);
	}

	@Override
	public CollisionBox buildCollisionBox() {
		
		return null;
	}

	@Override
	public boolean canMoveOnCollision() {
		return true;
	}

	@Override
	public Point3D getAllCorrections() {
		return null;//TODO REDEFINE THIS ASAP
	}
	
}
