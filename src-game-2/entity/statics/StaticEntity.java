package entity.statics;

import collision.CollisionGrid;
import entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import visual.Component;
import visual.TreeComponent;

public abstract class StaticEntity extends Entity {

	public StaticEntity(Point3D position) {
		super(position/*new Point3D(position.getX(), CollisionGrid.getInstance().getHeightAt(position), position.getZ())*/);
		
	}
	@Override
	public void update(double deltaTime) {
		
	}
	@Override
	public abstract Component buildComponent();
}
