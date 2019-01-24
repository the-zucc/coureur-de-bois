package entity;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import game.Map;
import javafx.geometry.Point3D;
import util.MessageCallback;
import util.PositionGenerator;
import visual.Component;

public abstract class VisibleCollidingEntity extends VisibleEntity implements Collideable, MessageReceiver {
	protected CollisionBox collisionBox;
	protected int collisionMapRow;
	public void setCollisionMapRow(int collisionMapRow){
		this.collisionMapRow = collisionMapRow;
	}
	@Override
	public int getCollisionMapRow() {
		return collisionMapRow;
	}
	protected int collisionMapColumn;
	public void setCollisionMapColum(int collisionMapColumn){
		this.collisionMapColumn = collisionMapColumn;
	}
	@Override
	public int getCollisionMapColumn() {
		return collisionMapColumn;
	}
	
	public VisibleCollidingEntity(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		
		collisionBox = buildCollisionBox();
		this.collisionMapRow = map.getCollisionRowFor(get2DPosition());
		this.collisionMapColumn = map.getCollisionColumnFor(get2DPosition());
	}
	
	@Override
	public abstract Component buildComponent();
	
	protected void correctCollisions(){
		Point3D corrections = getAllCorrections();
		if(corrections != null){
			moveTo(getPosition().add(corrections));
			if(getPosition().getY()>map.getHeightAt(get2DPosition())) {
				moveTo(PositionGenerator.getFloorPosition(get2DPosition(), map));
			}
				
		}
	}
	
	@Override
	public void moveTo(Point3D nextPosition) {
		setPosition(nextPosition);
		if(collisionBox != null){
			collisionBox.update();
		}
		
		int newCollisionMapRow = map.getCollisionRowFor(get2DPosition());
		int newCollisionMapColumn = map.getCollisionColumnFor(get2DPosition());
		if(newCollisionMapRow != collisionMapRow || newCollisionMapColumn != collisionMapColumn){
			try{
				map.getCollisionMap()[collisionMapRow][collisionMapColumn].remove(this);
				
			}catch(Exception e){
				//System.out.println("out of map");
			}
			collisionMapRow = newCollisionMapRow;
			collisionMapColumn = newCollisionMapColumn;
			try{
				map.getCollisionMap()[collisionMapRow][collisionMapColumn].add(this);
			}catch(Exception e){
				
			}
		}
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
		Point3D correction = Point3D.ZERO;
		CollisionBox cbox = c.getCollisionBox();
		if(collisionBox != null && cbox != null){
			correction = getCollisionBox().getCorrection(cbox);
		}
		return correction;
	}

	@Override
	public abstract boolean canMoveOnCollision();
	@Override
	public void update(double secondsPassed){
		super.update(secondsPassed);
		getCollisionBox().update();
	}
}
