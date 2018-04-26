package entity.living;

import characteristic.*;
import entity.GravityAffectedCollidingEntity;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

public abstract  class LivingEntity extends GravityAffectedCollidingEntity implements ComponentUpdateable{
	
	//private Point3D gravity;
	private double hp;
	private boolean up, down, left, right, newOrientation, isRunning;
	private Point3D oldMovement;
	private Point3D jumpVector;
	private Point2D target;
	protected double rotationAngle;
	protected double computeComponentRotationAngle(double rotationAngle){
		
		return rotationAngle+90;
	}

	public LivingEntity(Point3D position, Map map) {
		super(position, map);
		up = down = left = right = newOrientation = isRunning = false;
		oldMovement = Point3D.ZERO;
		movement = oldMovement;
		target = null;
		rotationAngle = 0;
	}

	protected abstract double computeXpReward();
	@Override
	public boolean shouldUpdate(){
		return true;
	}
	@Override
	public void update(double secondsPassed){
		updateMovementVector();
		super.update(secondsPassed);
		updateActions(secondsPassed);
		if(shouldUpdateComponent())
			updateComponent();
	}
	
	public abstract void updateActions(double secondsPassed);

	@Override
	public boolean shouldUpdateComponent(){
		return true;
	}
	@Override
	public void updateComponent(){
		getComponent().setPosition(getPosition());
		getComponent().setRotationAxis(Rotate.Y_AXIS);
		getComponent().setRotate(computeComponentRotationAngle(rotationAngle));
		additionalComponentUpdates();
	}

	public abstract void additionalComponentUpdates();

	public double getHp(){
		return hp;
	}

    protected void updateMovementVector() {
		oldMovement = movement;
		
		double movementSpeed = computeMovementSpeed();
		double angle = Math.toRadians(45);
		if(target == null){
			if(up) {
				if(up && right) {
					movement = new Point3D(Math.cos(angle) * movementSpeed, 0, Math.sin(angle) * movementSpeed);
				}
				else if(up && left) {
					movement = new Point3D(-Math.cos(angle) * movementSpeed, 0, Math.sin(angle) * movementSpeed);
				}
				else{
					movement = new Point3D(0, 0, movementSpeed);
				}
			}
			else if(down) {
				if(down && right) {
					movement = new Point3D(Math.cos(angle) * movementSpeed, 0, -Math.sin(angle) * movementSpeed);
				}
				else if(down && left) {
					movement = new Point3D(-Math.cos(angle) * movementSpeed, 0, -Math.sin(angle) * movementSpeed);
				}
				else{
					movement = new Point3D(0, 0, -movementSpeed);					
				}
				
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
		}
		else{
			if(position2D.distance(target) < movement.magnitude()){
				movement = Point3D.ZERO;
				target = null;
			}
			else{
				Point3D temp = new Point3D(target.getX(), 0, target.getY()).subtract(new Point3D(position2D.getX(), 0, position2D.getY())).normalize();
				movement = temp.multiply(1/temp.magnitude()).multiply(movementSpeed);
			}

		}
		rotationAngle = computeAngleFromMovement(movement);
	}
	/**
	 * this method should be defined in all subclasses. it returns the movement speed to use. The value can be dynamic, as the method is called every tick.
	 * @return the movement speed to use when computing the movmement vector for this entity.
	 */
	protected abstract double computeMovementSpeed();
	protected double computeAngleFromMovement(Point3D movement){
		if(movement.equals(Point3D.ZERO))
			return 90;
		double angle = new Point3D(movement.getX(), 0, movement.getZ()).angle(Rotate.X_AXIS);
		if(movement.getZ()>0)
			angle*=-1;
		return angle;
	}
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
		if(getPosition().getY() >= map.getHeightAt(get2DPosition())-0.1*GameLogic.getMeterLength()){
			this.addForceToGravity(getJumpVector());
		}
	}
	protected Point3D getJumpVector() {
		return jumpVector;
	}
	protected void startMovingTo(Point2D target){
		this.target = target;
	}
}
