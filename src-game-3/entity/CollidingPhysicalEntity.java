package entity;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class CollidingPhysicalEntity extends PhysicalEntity implements Collideable{
	

	public CollidingPhysicalEntity(Point3D position) {
		super(position);
	}

	@Override
	public abstract Component buildComponent();

	@Override
	public void update(double secondsPassed) {
		
	}

	@Override
	public boolean shouldUpdate() {
		return false;
	}

	@Override
	public void moveTo(Point3D nextPosition) {
		setPosition(nextPosition);
	}

	@Override
	public Point3D computeNextPosition() {
		
		return null;
	}

	@Override
	public CollisionBox buildCollisionBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollisionBox getCollisionBox() {
		// TODO Auto-generated method stub
		return null;
	}

}
