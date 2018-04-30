package entity.wearable;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.MovingCollidingEntity;
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

public class WeaponEntity extends MovingCollidingEntity implements Attachable {

	private AttachableReceiver receiver;
	private Point3D relativePosition;
	
	public WeaponEntity(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		this.relativePosition = new Point3D(0.5*GameLogic.getMeterLength(),-0.7*GameLogic.getMeterLength(),0);
		this.accept("player_position_3D", (params)->{
			Point3D playerPos = (Point3D)params[0];
			if(playerPos.distance(getPosition())<GameLogic.getMeterLength()){
				messenger.send("wield_weapon", params[1], this);
			}
		});
		this.accept("wielded", (params)->{
			
		});
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}

	@Override
	public void onHover(MouseEvent me) {
		
	}

	@Override
	public void onUnHover(MouseEvent me) {
		
	}

	@Override
	public void onClick(MouseEvent me) {
		/*if(receiver == null)
			messenger.send("wield_weapon", map.getCurrentPlayer(), this);
		 */
	}

	@Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	public void onAttach(AttachableReceiver ar) {
		getComponent().setTranslateX(this.getPositionRelativeToReceiver().getX());
		getComponent().setTranslateY(this.getPositionRelativeToReceiver().getY());
		getComponent().setTranslateZ(this.getPositionRelativeToReceiver().getZ());
		receiver = ar;
	}

	@Override
	public void onDetach(AttachableReceiver ar) {
		setPosition(receiver.getPosition().add(new Point3D(10,map.getHeightAt(receiver.get2DPosition()),10)));
		ticksSinceDrop=0;
		receiver = null;
		relativePosition = null;
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
		return receiver.getPosition().add(getPositionRelativeToReceiver());
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
	public void onCollides(Collideable c) {
		
	}

	@Override
	protected Cursor getHoveredCursor() {
		return null;
	}

	@Override
	protected String getMouseToolTipText() {
		return "Standard sword";
	}

	@Override
	protected Parent buildOnClickedPane() {
		return null;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		return null;
	}
	
	private int ticksSinceDrop = 0;
	@Override
	public void update(double secondsPassed){
		super.update(secondsPassed);
		if(getReceiver() == null){
			ticksSinceDrop++;
			double height = Math.sin(((double)ticksSinceDrop)/10)-GameLogic.getMeterLength();
			Point3D add = new Point3D(0,height,0);
			getComponent().setPosition(getPosition().add(add));
		}
		else{
			if(attacking){
				ticksSinceAttack++;
				getComponent().setRotationAxis(Rotate.X_AXIS);
				getComponent().setRotate(getComponent().getRotate()-10);
				if(ticksSinceAttack == 8){
					ticksSinceAttack = 0;
					attacking = false;
					getComponent().setRotate(0);
				}
			}
		}
	}
	private boolean attacking = false;
	private double ticksSinceAttack = 0;
	public void attack(MessageReceiver mr){
		MessageReceiver attacker = (MessageReceiver)getReceiver();
		messenger.send("damage", mr, computeDamage(), attacker);
		attacking = true;
	}
	protected double computeDamage(){
		return Math.random()*10+20;
	}
}
