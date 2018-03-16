package entity;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class VisibleCollidingEntity extends VisibleEntity implements Collideable{
	protected CollisionBox collisionBox;

	public VisibleCollidingEntity(Point3D position) {
		super(position);
		collisionBox = buildCollisionBox();
	}

	@Override
	public abstract Component buildComponent();
 
	@Override
	public void moveTo(Point3D nextPosition) {
		//System.out.println(nextPosition);
		setPosition(nextPosition);
	}

	@Override
	public abstract Point3D computeNextPosition(double secondsPassed);

	@Override
	public abstract CollisionBox buildCollisionBox();

	@Override
	public CollisionBox getCollisionBox() {
		return collisionBox;
	}

	@Override
	public abstract void onCollides(Collideable c);

	@Override
	public boolean collides(Collideable c) {
		return this.getCollisionBox().collides(c.getCollisionBox());
	}

	@Override
	public Point3D getCorrection(Collideable c) {
		return getCollisionBox().getCorrection(c.getCollisionBox());
	}

	@Override
	public abstract boolean canMoveOnCollision();

}
