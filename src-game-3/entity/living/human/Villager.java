package entity.living.human;

import characteristic.interactive.Hoverable;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import visual.Component;

public class Villager extends Human implements Hoverable{

	public Villager(Point3D position) {
		super(position, (int)(Math.random()*10)+1);
	}

	

	@Override
	public void updateComponent() {
		getComponent().setTranslateX(position.getX());
		getComponent().setTranslateY(position.getY());
		getComponent().setTranslateZ(position.getZ());
	}

	@Override
	protected double computeXpReward() {

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
		Component returnVal = new Component(getId());

		returnVal.setOnMouseEntered(e -> {
			this.onHover(e);
		});
		returnVal.setOnMouseExited(e -> {
			this.onUnHover(e);
		});
		return null;
	}

	@Override
	public CollisionBox buildCollisionBox() {

		return null;
	}

	@Override
	public void onCollides(Collideable c) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHover(MouseEvent me) {

	}

	@Override
	public void onUnHover(MouseEvent me) {

	}
}
