package entity.living.animal;

import entity.living.LivingEntity;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class Animal extends LivingEntity {

	protected Animal(Point3D position) {
		super(position);
	}

	@Override
	public void update(double deltaTime) {
		
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

}
