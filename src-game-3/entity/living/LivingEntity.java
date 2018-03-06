package entity.living;

import java.util.Hashtable;

import characteristic.*;
import characteristic.positionnable.GravityAffected;
import collision.CollisionBox;
import entity.GravityAffectedCollidingEntity;
import entity.VisibleCollidingEntity;
import game.GameLogic;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import visual.Component;

public abstract  class LivingEntity extends GravityAffectedCollidingEntity implements ComponentUpdateable{
	
	//private Point3D gravity;
	private double hp;
	private boolean up, down, left, right, newOrientation, isRunning;
	private Point3D oldMovement;

	public LivingEntity(Point3D position) {
		super(position);
		up = down = left = right = newOrientation = isRunning = false;
		oldMovement = Point3D.ZERO;
	}

	protected abstract double computeXpReward();
	
	@Override
	public void update(double secondsPassed){
		updateMovementVector();
		Point3D nextPos = computeNextPosition(secondsPassed);
		if(nextPos != null){
			System.out.println(nextPos);
			moveTo(nextPos);
		}
		updateActions(secondsPassed);
		if(shouldUpdateComponent())
			updateComponent();
	}
	
	public abstract void updateActions(double secondsPassed);
	
	public double getHp(){
		return hp;
	}

	@Override
	public void onMessageReceived(Hashtable<String, ? extends Object> message) {
		/**
		 * cmputes the operations when the entity receives damage
		 */
		if(message.containsKey("damage")){
			hp -= ((Hashtable<String, Double>)message).get("damage");
			if(hp<=0){
				if(message.containsKey("sender")){
					Hashtable<String, ? extends Object> sentMessage = GameLogic.createSimpleXpMessage(computeXpReward(), this);
					
					Messageable destination = ((Hashtable<String, Messageable>)sentMessage).get("sender");
					
					GameLogic.sendMessage(destination, sentMessage);
				}
			}
		}
	}
	protected void updateMovementVector() {
		double movementSpeed = computeMovementSpeed();
		double angle = Math.toRadians(45);
		if(up) {
			if(up && right) {
				movement = new Point3D(Math.cos(angle) * movementSpeed, 0, Math.sin(angle) * movementSpeed);
				return;
			}
			else if(up && left) {
				movement = new Point3D(-Math.cos(angle) * movementSpeed, 0, Math.sin(angle) * movementSpeed);
				return;
			}
			movement = new Point3D(0, 0, movementSpeed);
			return;
		}
		else if(down) {
			if(down && right) {
				movement = new Point3D(Math.cos(angle) * movementSpeed, 0, -Math.sin(angle) * movementSpeed);
				return;
			}
			else if(down && left) {
				movement = new Point3D(-Math.cos(angle) * movementSpeed, 0, -Math.sin(angle) * movementSpeed);
				return;
			}
			movement = new Point3D(0, 0, -movementSpeed);
			return;
		}
		else if(right)
			movement = new Point3D(movementSpeed, 0, 0);
		else if(left)
			movement = new Point3D(-movementSpeed, 0, 0);
		else
			movement = new Point3D(0,0,0);
		if(oldMovement.angle(Rotate.X_AXIS) == movement.angle(Rotate.X_AXIS))
			newOrientation = false;
		else
			newOrientation = true;
		oldMovement = movement;
	}
	/**
	 * this method should be defined in all subclasses. it returns the movement speed to use. The value can be dynamic, as the method is called every tick.
	 * @return the movement speed to use when computing the movmement vector for this entity.
	 */
	protected abstract double computeMovementSpeed();
	
	protected void setUp(boolean value) {
		up = value;
		newOrientation = true;
	}
	protected void setDown(boolean value) {
		down = value;
		newOrientation = true;
	}
	protected void setLeft(boolean value) {
		left = value;
		newOrientation = true;
	}
	protected void setRight(boolean value) {
		right = value;
		newOrientation = true;
	}
	protected void setIsRunning(boolean value) {
		isRunning = value;
	}
	protected boolean isRunning(){
		return isRunning;
	}
	protected void jump(){
		this.addForceToGravity(getJumpVector());
	}
	protected abstract Point3D getJumpVector();
}
