package entity;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import util.Updateable;

public class Mob extends GameElement implements Updateable {
	private double hp;
	private double xpReward;
	private int level;

	public Mob(Point3D position, double hp, double xpReward, int level, Color color) {
		super(position);
		this.hp = hp;
		this.xpReward = xpReward;
		this.level = level;
	}
	@Override
	public void update(double deltaTime) {
		
	}
	
}
