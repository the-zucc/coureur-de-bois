package entity.living.human;

import entity.living.LivingEntity;
import javafx.geometry.Point3D;

public abstract class Human extends LivingEntity {
	int level;

	public Human(Point3D position, int level) {
		super(position);
		this.level = level;
	}
	@Override
	public boolean shouldUpdateComponent() {
		return true;
	}
	@Override
	public boolean shouldUpdate(){
		return true;
	}
}
