package entity;

import java.util.ArrayList;
import java.util.Hashtable;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.living.human.Player;
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
		if(this instanceof Player){
			int collideableCount = 0;
			System.out.println("boi correction");
			Point3D corrections = Point3D.ZERO;
			for(int i = collisionMapRow-1; i <= collisionMapRow+1; i++){
				for(int j = collisionMapColumn-1; j <= collisionMapColumn+1;j++){
					try{
						ArrayList<Collideable> collideables = map.getCollisionMap()[i][j];
						for(int k = 0; k < collideables.size(); k++){
						Collideable c = collideables.get(k);
						collideableCount++;
						if(c.getCollisionBox() != null && getCollisionBox() != null && c != this)
							if(getCollisionBox().collides(c.getCollisionBox())){
								System.out.println("collision");
								corrections = corrections.add(this.getCorrection(c));
							}
						}
						
					}catch(Exception e){
						return Point3D.ZERO;
					}
				}
			}
			System.out.println(collideableCount);
			if(corrections != null)
				return corrections;
			else
				return Point3D.ZERO;
		}
		return Point3D.ZERO;
	}
	public void update(double secondsPassed){
		Point3D nextPos = computeNextPosition(secondsPassed);
		moveTo(nextPos);
		collisionBox.update();
		correctCollisions();
	}
}
