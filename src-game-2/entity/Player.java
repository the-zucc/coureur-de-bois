package entity;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import util.Updateable;
import visual.GameComponent;
import visual.PlayerComponent;

public class Player extends GameElement implements Updateable {
	
	private static double movementSpeed = 10; 
	private Color hatColor;
	private Point3D movementVector;
	private Point3D gravityJumpVector;
	boolean up, down, left, right, isRunning, isJumping, newOrientation;
	
	public Player(Point3D position, Color hatColor) {
		super(position);
		this.hatColor = hatColor;
	}
	
	
	@Override
	public void update(double deltaTime) {
		updateMovementVector();
		runIfIsRunning();
		updateGravityJumpVector();
		move();
	}

	@Override
	public GameComponent buildComponent() {
		return new PlayerComponent(hatColor);
	}
	
	public void setUp(boolean value) {
		up = value;
		newOrientation = true;
	}
	public void setDown(boolean value) {
		down = value;
		newOrientation = true;
	}
	public void setLeft(boolean value) {
		left = value;
		newOrientation = true;
	}
	public void setRight(boolean value) {
		right = value;
		newOrientation = true;
	}
	public void setIsRunning(boolean value) {
		isRunning = value;
	}
	
	private void move() {
		if(movementVector != null)
			position = position.add(movementVector);
		if(gravityJumpVector != null)
			position = position.add(gravityJumpVector);
	}
	/**
	 * updates the movement vector with the new orientation of the player using the boolean values up, down left, right
	 */
	private void updateMovementVector() {
		double angle = Math.toRadians(45);
		if(up) {
			if(up && right) {
				movementVector = new Point3D(Math.cos(angle) * movementSpeed, 0, Math.sin(angle) * movementSpeed);
				return;
			}
			else if(up && left) {
				movementVector = new Point3D(-Math.cos(angle) * movementSpeed, 0, Math.sin(angle) * movementSpeed);
				return;
			}
			movementVector = new Point3D(0, 0, movementSpeed);
			return;
		}
		else if(down) {
			if(down && right) {
				movementVector = new Point3D(Math.cos(angle) * movementSpeed, 0, -Math.sin(angle) * movementSpeed);
				return;
			}
			else if(down && left) {
				movementVector = new Point3D(-Math.cos(angle) * movementSpeed, 0, -Math.sin(angle) * movementSpeed);
				return;
			}
			movementVector = new Point3D(0, 0, -movementSpeed);
			return;
		}
		else if(right)
			movementVector = new Point3D(movementSpeed, 0, 0);
		else if(left)
			movementVector = new Point3D(-movementSpeed, 0, 0);
		else
			movementVector = null;
	}
	private void runIfIsRunning() {
		if(movementVector != null)
			if(isRunning)
				movementVector = movementVector.add(movementVector);
	}
	private void updateGravityJumpVector(){
		
	}
}
