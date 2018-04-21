package entity;

import java.util.ArrayList;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.living.human.Player;
import game.Map;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class VisibleCollidingEntity extends VisibleEntity implements Collideable{
	protected CollisionBox collisionBox;
	protected Map map;
	protected int collisionMapRow;
	public void setCollisionMapRow(int collisionMapRow){
		this.collisionMapRow = collisionMapRow;
	}
	protected int collisionMapColumn;
	public void setCollisionMapColum(int collisionMapColumn){
		this.collisionMapColumn = collisionMapColumn;
	}
	
	public VisibleCollidingEntity(Point3D position, Map map) {
		super(position);
		collisionBox = buildCollisionBox();
		this.map = map;
		this.collisionMapRow = map.getCollisionRowFor(get2DPosition());
		this.collisionMapColumn = map.getCollisionColumnFor(get2DPosition());
	}
	
	@Override
	public abstract Component buildComponent();
	
	protected void correctCollisions(){
		Point3D corrections = getAllCorrections();
		//System.out.println("corrections "+getClass().getName()+" "+corrections);
		if(corrections != null){
			moveTo(getPosition().add(corrections));
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
				collisionMapRow = newCollisionMapRow;
				collisionMapColumn = newCollisionMapColumn;
				map.getCollisionMap()[collisionMapRow][collisionMapColumn].add(this);
			}catch(Exception e){
				//System.out.println("out of map");
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
}
