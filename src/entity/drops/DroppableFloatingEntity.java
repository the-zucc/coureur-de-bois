package entity.drops;

import characteristic.Messenger;
import entity.VisibleEntity;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import visual.Component;

public abstract class DroppableFloatingEntity extends VisibleEntity{

	public DroppableFloatingEntity(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		this.accept("position_3D", (params)->{
			if(canBePickedUp()) {
				Point3D playerPos = (Point3D)params[0];
				if(playerPos.distance(getPosition())<GameLogic.getMeterLength()){
					onPickup(params);
				}
			}
		});
	}
	protected abstract boolean canBePickedUp();
	protected abstract void onPickup(Object... params);
	protected double ticksSinceDrop = 0;
	@Override
	public void update(float secondsPassed) {
		super.update(secondsPassed);
		if(shouldFloat()){
			double height = Math.sin(((double)ticksSinceDrop)/10)-GameLogic.getMeterLength();
			Point3D add = new Point3D(0,height,0);
			getComponent().setPosition(getPosition().add(add));
			getComponent().setRotationAxis(Rotate.Y_AXIS);
			getComponent().setRotate(getComponent().getRotate()+1);
		}
			
	}
	protected boolean shouldFloat() {
		return true;
	}
	@Override
	public boolean shouldUpdate() {
		return true;
	}
}
