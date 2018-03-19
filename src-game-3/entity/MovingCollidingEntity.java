package entity;

import java.util.Hashtable;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import game.Map;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class MovingCollidingEntity extends VisibleCollidingEntity{

	protected Point3D movement;
	
	public MovingCollidingEntity(Point3D position, Map map) {
		super(position, map);
	}

	@Override
	public abstract Component buildComponent();

	@Override
	public Point3D computeNextPosition(double secondsPassed) {
		if(movement != null){
			return getPosition().add(movement.multiply(secondsPassed));
		}
		else return position;
	}

	@Override
	public abstract CollisionBox buildCollisionBox();

	@Override
	public boolean canMoveOnCollision() {
		return true;
	}

	@Override
	public Point3D getAllCorrections() {
		return null;//TODO REDEFINE THIS ASAP
	}
	public void update(double secondsPassed){
		Point3D nextPos = computeNextPosition(secondsPassed);
		moveTo(nextPos);
		correctCollisions();
	}
}
