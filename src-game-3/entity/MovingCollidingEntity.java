package entity;

import java.util.ArrayList;
import java.util.Hashtable;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.living.human.Player;
import game.Map;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class MovingCollidingEntity extends VisibleCollidingEntity{

	protected Point3D movement;
	
	public MovingCollidingEntity(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
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
		Point3D corrections = Point3D.ZERO;
		for(int i = collisionMapRow-1; i <= collisionMapRow+1; i++){
			for(int j = collisionMapColumn-1; j <= collisionMapColumn+1;j++){
				try{
					ArrayList<Collideable> collideables = map.getCollisionMap()[i][j];
					for(int k = 0; k < collideables.size(); k++){
						Collideable c = collideables.get(k);
						if(c.getCollisionBox() != null && getCollisionBox() != null && c != this) {
							if(getCollisionBox().collides(c.getCollisionBox())){
								corrections = corrections.add(this.getCorrection(c));
							}
						}
					}
				}catch(ArrayIndexOutOfBoundsException e){
					
				}catch(Exception e){
					e.printStackTrace();
					return Point3D.ZERO;
				}
			}
		}
		if(corrections != null)
			return corrections;
		else
			return Point3D.ZERO;
	}
	@Override
	public void update(double secondsPassed){
		processCallbackQueue();
		super.update(secondsPassed);
		Point3D nextPos = computeNextPosition(secondsPassed);
		moveTo(nextPos);
		if(collisionBox != null){
			collisionBox.update();
		}
		correctCollisions();
	}
}
