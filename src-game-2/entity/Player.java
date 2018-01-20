package entity;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import util.Updateable;
import visual.GameComponent;
import visual.PlayerComponent;

public class Player extends GameElement implements Updateable {
	
	private Color hatColor;
	
	public Player(Point3D position, Color hatColor) {
		super(position);
		this.hatColor = hatColor;
	}
	boolean up, down, left, right, isRunning, isJumping, newOrientation;
	
	@Override
	public void update(double deltaTime) {
		
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
}
