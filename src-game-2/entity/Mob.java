package entity;

import java.util.concurrent.ThreadLocalRandom;

import app.Model;
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
		
		//movement = new Point3D(ThreadLocalRandom.current().nextInt(), 0, ThreadLocalRandom.current().nextInt()).normalize();
	}
	@Override
	public void update(double deltaTime) {
		move();
	}
	@Override
	public GameComponent buildComponent() {
		return new MobComponent(color);
	}
	private void move() {
		if(movement != null)
			position = position.add(movement);
	}
	public double getHp() {
		return hp;
	}
}
