package entity;

import java.util.concurrent.ThreadLocalRandom;

import app.Model;
import collision.SphericalCollisionBox;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import util.Updateable;
import visual.GameComponent;
import visual.MobComponent;

public class Mob extends LivingEntity{
	
	private int level;
	private Color color;
	private Point3D target;

	public Mob(Point3D position, double hp, double xpReward, int level, Color color) {
		super(position);
		this.hp = hp;
		this.xpReward = xpReward;
		this.level = level;
		this.color = color;
		this.collisionBox = new SphericalCollisionBox(getId(),position, 20);
		
		//debug
		//movement = new Point3D(ThreadLocalRandom.current().nextInt(), 0, ThreadLocalRandom.current().nextInt()).normalize();
		
	}
	@Override
	public void update(double deltaTime) {
		move();
		fall();
		updateAngleDegrees();
		updateCollisionGrid();
		correctCollisions();
		Point3D point = Model.getInstance().getCurrentPlayer().getPosition();
		if(position.distance(point) < 500)
			setTarget(point);
		if(ThreadLocalRandom.current().nextDouble()<0.0003)
			jump();
		//else
			//movement = new Point3D(ThreadLocalRandom.current().nextDouble()/10, 0, ThreadLocalRandom.current().nextDouble()/10);
	}
	@Override
	protected void move(){
		super.move();
		if(movement != null && target != null){
			if(position.distance(target) < movement.distance(Point3D.ZERO)){
				movement = null;
			}
		}
	}
	@Override
	public GameComponent buildComponent() {
		return new MobComponent(color);
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
	@Override
	protected void updateMovementVector() {
		
	}
	@Override
	public double getXpReward() {
		return xpReward;
	}
	public void setTarget(Point3D target){
		this.target = target;
		movement = target.subtract(position).normalize();
		//System.out.println(movement);
	}
}
