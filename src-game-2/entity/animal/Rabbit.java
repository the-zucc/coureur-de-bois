package entity.animal;

import entity.LivingEntity;
import javafx.geometry.Point3D;
import visual.GameComponent;

public class Rabbit extends LivingEntity implements Prey {

	protected Rabbit(Point3D position) {
		super(position);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(double deltaTime) {
		
	}

	@Override
	public GameComponent buildComponent() {
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
	public void function_test_prey() {
		// TODO Auto-generated method stub
		
	}

}
