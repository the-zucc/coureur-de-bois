package visual;

import entity.Entity;
import entity.living.LivingEntity;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

public abstract class LivingComponent extends Component implements UpdateableComponent{

	@Override
	public void update(Entity e) {
		if(e instanceof LivingEntity) {
			Point3D position = e.getPosition();
			setTranslateX(position.getX());
			setTranslateY(position.getY());
			setTranslateZ(position.getZ());
			setRotationAxis(Rotate.Y_AXIS);
			setRotate(((LivingEntity) e).getAngleDegrees()+90);
		}
		else
			System.out.println("wrong entity type for component");
	}
	
}
