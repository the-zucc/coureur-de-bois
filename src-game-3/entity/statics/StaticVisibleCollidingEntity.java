package entity.statics;

import java.util.Hashtable;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.VisibleCollidingEntity;
import game.Map;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class StaticVisibleCollidingEntity extends VisibleCollidingEntity {

	public StaticVisibleCollidingEntity(Point3D position, Map map) {
		super(position, map);
	}

	@Override
	public abstract Component buildComponent();

	@Override
	public Point3D computeNextPosition(double secondsPassed) {
		return position;
	}

	@Override
	public abstract CollisionBox buildCollisionBox();

	@Override
	public abstract void onCollides(Collideable c);

	@Override
	/**
	 * returns false because the entity is static, which means it does not move.
	 */
	public boolean canMoveOnCollision() {
		return false;
	}

	@Override
	public abstract void onMessageReceived(Hashtable<String, ? extends Object> message);
}
