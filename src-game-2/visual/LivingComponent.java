package visual;

import app.Model;
import entity.Entity;
import entity.LivingEntity;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import util.Updateable;

public abstract class LivingComponent extends GameComponent implements UpdateableComponent{

	@Override
	public void update(Entity e) {
		if(e instanceof LivingEntity) {
			Point3D position = e.getPosition();
			setTranslateX(position.getX());
			setTranslateY(position.getY());
			setTranslateZ(position.getZ());
			setRotationAxis(Rotate.Y_AXIS);
			setRotate(((LivingEntity) e).getAngleDegrees());
		}
		else
			System.out.println("wrong entity type for component");
	}
	
}
