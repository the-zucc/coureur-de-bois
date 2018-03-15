package entity.living.human;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import javafx.geometry.Point3D;
import village.Village;
import visual.Component;

public class Villager extends Human {
	
	private Village village;

	public Villager(Point3D position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

	

	@Override
	public void updateComponent() {
		// TODO Auto-generated method stub

	}

	@Override
	protected double computeXpReward() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateActions(double secondsPassed) {
		// TODO Auto-generated method stub

	}

	@Override
	protected double computeMovementSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public Component buildComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CollisionBox buildCollisionBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollides(Collideable c) {
		// TODO Auto-generated method stub

	}
	
	public double getDistanceFromVillage(){
		
		return get2DPosition().distance(village.get2DPosition());
	}
}
