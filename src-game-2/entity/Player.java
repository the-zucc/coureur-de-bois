package entity;

import collision.SphericalCollisionBox;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import util.Updateable;
import visual.GameComponent;
import visual.PlayerComponent;

public class Player extends LivingEntity implements Updateable {
	
	private static double movementSpeed = 10;
	private Color hatColor;
	private boolean up, down, left, right, newOrientation;
	
	public Player(Point3D position, Color hatColor) {
		super(position);
		this.hatColor = hatColor;
		collisionBox = new SphericalCollisionBox(getId(), position, 20);
	}
	
	@Override
	public void update(double deltaTime) {
		updateMovementVector();
		runIfIsRunning();
		updateAngleDegrees();
		updateGravityJumpVector();
		move();
		updateCollisionGrid();
		correctCollisions();
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
	/**
	 * updates the movement vector with the new orientation of the player using the boolean values up, down left, right
	 */
	protected void updateMovementVector() {
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
			movement = null;
	}
	
	private void runIfIsRunning() {
		if(movement != null)
			if(isRunning)
				movement = movement.add(movement);
	}
	
	@Override
	public double getXpReward() {
		return 100 * level + maxHp;
	}
}
