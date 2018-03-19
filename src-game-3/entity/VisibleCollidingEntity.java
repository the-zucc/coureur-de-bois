package entity;

import java.util.ArrayList;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import game.Map;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class VisibleCollidingEntity extends VisibleEntity implements Collideable{
	protected CollisionBox collisionBox;
	protected Map map;
	private int collisionMapRow;
	private int collisionMapColumn;
	
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
		if(corrections != null){
			moveTo(getPosition().add(corrections));
		}
	}
	
	@Override
	public void moveTo(Point3D nextPosition) {
		//System.out.println(nextPosition);
		setPosition(nextPosition);
		if(collisionBox != null){
			collisionBox.update();
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
		Point3D correction = null;
		CollisionBox cbox = c.getCollisionBox();
		if(collisionBox != null && cbox != null){
			correction = getCollisionBox().getCorrection(c.getCollisionBox());
		}
		return correction;
	}

	@Override
	public abstract boolean canMoveOnCollision();
	
	@Override
	public Point3D getAllCorrections(){
		Point3D corrections = Point3D.ZERO;
		if(!collisionBox.isBigCollisionBox()){
			ArrayList<Collideable>[][] nearbyCollideables = map.getNearbyCollideables(collisionMapRow, collisionMapRow);
			for(ArrayList<Collideable>[] a:nearbyCollideables){
				for(ArrayList<Collideable> b:a){
					for (Collideable collideable : b) {
						if(collideable != this){
							Point3D correction = getCorrection(collideable);
							System.out.println(correction);
							corrections = corrections.add(correction);
						}
					}
				}
			}
		}
		for(Collideable c:map.getBigCollideables()){
			if(c != this){
				corrections = corrections.add(getCorrection(c));
			}
		}
		return position;
	}
}
