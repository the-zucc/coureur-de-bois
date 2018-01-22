package entity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import app.Model;
import collision.CollisionBox;
import collision.CollisionGrid;
import collision.SphericalCollisionBox;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import util.Updateable;
import visual.GameComponent;
import visual.MobComponent;

public class Mob extends GameElement implements Updateable {
	private double hp;
	private double xpReward;
	private int level;
	private Color color;
	private Point3D movement;

	public Mob(Point3D position, double hp, double xpReward, int level, Color color) {
		super(position);
		this.hp = hp;
		this.xpReward = xpReward;
		this.level = level;
		this.color = color;
		this.collisionBox = new SphericalCollisionBox(position, 20);
		movement = new Point3D(ThreadLocalRandom.current().nextInt(), 0, ThreadLocalRandom.current().nextInt()).normalize();
	}
	@Override
	public void update(double deltaTime) {
		move();
		updateCollisionGrid();
		correctCollisions();
	}
	@Override
	public GameComponent buildComponent() {
		return new MobComponent(color);
	}
	private void move() {
		if(movement != null)
			position = position.add(movement);
	}
	/**
	 * removes the collisionBox from its old section in the CollisionGrid instance,
	 * and adds the collisionBox to its new section. This is for optimizing, to
	 * ensure that the program does not check for collisions for each and every object
	 * with each and every other object, and checks only for objects that are near the current
	 * object's position. 
	 */
	
	public double getHp() {
		return hp;
	}
}
