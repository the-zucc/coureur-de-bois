package entity.living.human;

import app.Model;
import collision.SphericalCollisionBox;
import entity.UserControllable;
import entity.living.LivingEntity;
import javafx.geometry.Point3D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import util.Updateable;
import visual.Component;
import visual.PlayerComponent;

public class Player extends LivingEntity implements UserControllable{
	
	private static double movementSpeed = 5;
	private Color hatColor;
	
	
	public Player(Point3D position, Color hatColor) {
		super(position);
		this.hatColor = hatColor;
		collisionBox = new SphericalCollisionBox(getId(), position, 20);
	}
	
	@Override
	public void update(double deltaTime) {
		updateMovementVector();
		runIfIsRunning();
		fall();
		updateAngleDegrees();
		updateGravityJumpVector();
		move();
		updateCollisionGrid();
		correctCollisions();
		System.out.println(position);
	}

	@Override
	public Component buildComponent() {
		return new PlayerComponent(hatColor);
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

	@Override
	public void onMouseMoved(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyDown(KeyEvent ke) {
		// TODO Auto-generated method stub
		KeyCode code = ke.getCode();
		if(code.equals(KeyCode.W))
			this.setUp(true);
		else if(code.equals(KeyCode.A))
			this.setLeft(true);
		else if(code.equals(KeyCode.S))
			this.setDown(true);
		else if(code.equals(KeyCode.D))
			this.setRight(true);
		else if(code.equals(KeyCode.SHIFT))
			this.setIsRunning(true);
		else if(code.equals(KeyCode.SPACE))
			this.jump();
		ke.consume();
	}

	@Override
	public void onKeyUp(KeyEvent ke) {
		
	}

	@Override
	public void onMouseClicked(MouseEvent me) {
		// TODO Auto-generated method stub
		
	}
}
