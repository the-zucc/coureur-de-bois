package entity.wearable;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.MovingCollidingEntity;
import entity.VisibleEntity;
import entity.drops.DroppableFloatingEntity;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import visual.Component;

public abstract class WeaponEntity extends DroppableFloatingEntity implements Attachable {

	private AttachableReceiver receiver;
	private Point3D relativePosition;
	
	public WeaponEntity(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		this.relativePosition = computeWieldedPosition();
		accept("position_3D", (params)->{
			if(receiver == null) {
				Point3D playerPos = (Point3D)params[0];
				if(playerPos.distance(getPosition())<GameLogic.getMeterLength()){
					messenger.send("wield_weapon", params[1], this);
				}
			}
		});
	}
	@Override
	protected boolean canBePickedUp() {
		return receiver == null;
	}
	@Override
	protected void onPickup(Object... params ) {
		if(this.receiver == null) {
			messenger.send("wield_weapon", params[1], this);			
		}
	}
	
	protected abstract Point3D computeWieldedPosition();
	
	@Override
	public boolean shouldUpdate() {
		return true;
	}

	@Override
	public void onClick(MouseEvent me) {
		/*if(receiver == null)
			messenger.send("wield_weapon", map.getCurrentPlayer(), this);
		 */
	}

	@Override
	public void onAttach(AttachableReceiver ar) {
		ticksSinceDrop = 0;
		getComponent().setRotationAxis(Rotate.Y_AXIS);
		getComponent().setRotate(ticksSinceDrop);
		getComponent().setTranslateX(this.getPositionRelativeToReceiver().getX());
		getComponent().setTranslateY(this.getPositionRelativeToReceiver().getY());
		getComponent().setTranslateZ(this.getPositionRelativeToReceiver().getZ());
		receiver = ar;
	}

	@Override
	public void onDetach(AttachableReceiver ar) {
		setPosition(receiver.getPosition().add(new Point3D(10,0,10)));
		ticksSinceDrop=0;
		receiver = null;
		map.removeEntity(this);
		map.addEntity(this);
	}

	@Override
	public AttachableReceiver getReceiver() {
		return receiver;
	}

	@Override
	public Point3D getPositionRelativeToReceiver() {
		return relativePosition;
	}

	@Override
	public Point3D computeAbsolutePosition() {
		if(receiver != null){
			return receiver.getPosition().add(getPositionRelativeToReceiver());
		}
		return getPosition();
	}

	@Override
	protected Cursor getHoveredCursor() {
		return null;
	}



	@Override
	protected Parent buildOnClickedPane() {
		return null;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		return null;
	}
	
	@Override
	public void update(double secondsPassed){
		super.update(secondsPassed);

	}
	
	public void attack(MessageReceiver mr){
		MessageReceiver attacker = (MessageReceiver)getReceiver();
		messenger.send("damage", mr, computeDamage(), attacker);
	}
	protected double computeDamage(){
		return Math.random()*10+20;
	}
	@Override
	protected boolean shouldFloat() {
		return getReceiver() == null;
	}
}
