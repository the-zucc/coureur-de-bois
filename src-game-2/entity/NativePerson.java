package entity;

import entity.living.LivingEntity;
import javafx.geometry.Point3D;
import visual.Component;

public class NativePerson extends LivingEntity {

	protected NativePerson(Point3D position) {
		super(position);
	}

	@Override
	public Component buildComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateMovementVector() {
		// TODO Auto-generated method stub

	}

	@Override
	public double getXpReward() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(double deltaTime) {
		// TODO Auto-generated method stub
		
	}
	
}
