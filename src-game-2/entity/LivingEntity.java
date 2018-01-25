package entity;

import app.Model;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import visual.GameComponent;

public abstract class LivingEntity extends Entity {
	
	protected double hp;//current health points
	
	protected double maxHp = 100;//maximum health points
	
	protected double angleDegrees;//angle of rotation of the entity
	
	protected boolean isRunning, isJumping;
	
	protected double damage = 20;//damage of the basic attack
	
	protected double xpReward = 100;
	
	protected double xp = 0;
	
	protected double maxXp = 200;
	
	protected int level; 
	
	protected Point3D gravityAndJump;
	
	protected Point3D movement;
	
	
	
	
	protected LivingEntity(Point3D position) {
		super(position);
		hp = maxHp;
	}

	@Override
	public abstract GameComponent buildComponent();
	
	protected void move() {
		if(movement != null)
			position = position.add(movement);
	}
	
	protected double getHpRatio() {
		return hp/maxHp;
	}
	
	protected void updateGravityJumpVector(){
		
	}
	
	protected void fall() {
		
	}
	
	protected void updateAngleDegrees() {
		if(movement != null)
			angleDegrees = new Point3D(movement.getX(), 0, movement.getZ()).angle(Rotate.X_AXIS);
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
