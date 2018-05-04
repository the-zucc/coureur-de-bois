package entity.wearable;

import characteristic.Messenger;
import collision.CollisionBox;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import visual.Component;

public class StandardSword extends WeaponEntity{
	public StandardSword(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		Box sword = new Box(0.1*meter, meter,0.1*meter);
		sword.setTranslateY(-sword.getHeight());
		returnVal.getChildren().add(sword);
		return returnVal;
	}
	
	@Override
	public CollisionBox buildCollisionBox() {
		return null;
	}

	@Override
	protected Point3D computeWieldedPosition() {
		return new Point3D(0.5*GameLogic.getMeterLength(),0,0);
	}

	@Override
	protected void playAttackAnimation() {
		Component c = getComponent();
		animator.animate(()->{
			c.setRotationAxis(Rotate.X_AXIS);
			c.setRotate(c.getRotate()-6);
		}, 8).then(()->{
			c.setRotationAxis(Rotate.X_AXIS);
			c.setRotate(c.getRotate()+6);
		}, 8);
	}
}
