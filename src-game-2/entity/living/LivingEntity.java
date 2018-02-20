package entity.living;

import app.GameLogic;
import app.Model;
import entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import util.Updateable;
import visual.Component;

public abstract class LivingEntity extends Entity implements Updateable{
	
	protected double hp;//current health points
	
	protected double maxHp = 100;//maximum health points
	
	protected double angleDegrees;//angle of rotation of the entity
	
	protected boolean isRunning, isJumping;
	
	protected double damage = 20;//damage of the basic attack
	
	protected double xpReward = 100;
	
	protected double xp = 0;
	
	protected double maxXp = 200;
	
	protected int level; 
	
	protected Point3D movement;
	
	protected boolean up, down, left, right, newOrientation;
	
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
	
	
	protected LivingEntity(Point3D position) {
		super(position);
		hp = maxHp;
	}

	@Override
	public abstract Component buildComponent();
	
	public void jump(){
		this.gravity = new Point3D(GameLogic.getJump().getX(), GameLogic.getJump().getY(), GameLogic.getJump().getZ());
	}
	
	protected void setUp() {
		
	}
	
	protected void move() {
		if(movement != null)
			position = position.add(movement);
	}
	
	protected double getHpRatio() {
		return hp/maxHp;
	}
	
	protected void updateGravityJumpVector(){
		
	}
	
	protected void updateAngleDegrees() {
		if(movement != null)
			angleDegrees = new Point3D(movement.getX(), 0, movement.getZ()).angle(Rotate.X_AXIS);
		else
			angleDegrees = -90;
		//System.out.println(angleDegrees);
	}
	
	public double getAngleDegrees() {
		return angleDegrees;
	}
	
	protected abstract void updateMovementVector();
	
	public void attack(LivingEntity e) {
		e.dealDamage(damage, this);
	}
	
	public abstract double getXpReward();
	
	public void dealDamage(double damage, LivingEntity attacker) {
		hp-=damage;
		if(hp <= 0) {
			Model.getInstance().removeElement(getId());
			if(attacker != null)
				attacker.giveXp(getXpReward());
		}
	}
	
	public void giveXp(double amount) {
		xp+=amount;
		if(xp >= maxXp) {
			levelup();
			xp = amount-maxXp;
		}
		System.out.println("xp:"+xp+" level:"+level);
	}
	
	private void levelup() {
		level++;
	}
}
