package entity.living;

import java.util.Hashtable;

import characteristic.ComponentUpdateable;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.GravityAffected;
import collision.CollisionBox;
import entity.GravityAffectedCollidingEntity;
import entity.VisibleCollidingEntity;
import game.GameLogic;
import javafx.geometry.Point3D;
import visual.Component;

public  class LivingEntity extends GravityAffectedCollidingEntity implements GravityAffected, ComponentUpdateable{
	private Point3D gravity;
	private Point3D movement;
	private double hp;

	public LivingEntity(Point3D position) {
		super(position);
	}
	
	protected abstract double computeXpReward();
	
	public double getHp(){
		return hp;
	}
	private void dealDamage(double damage, LivingEntity attacker){
		hp+=damage;
		if(hp<=0){
			if(attacker != null){
				Hashtable<String, Double> message = GameLogic.createSimpleXpMessage(computeXpReward());
				GameLogic.sendMessage(attacker, message);
			}
		}
	}

	@Override
	public Component buildComponent() {
		
		return null;
	}

	@Override
	public void onMessageReceived(Hashtable<String, ? extends Object> message) {
		if(message.containsKey("damage")){
			hp -= ((Hashtable<String, Double>)message).get("damage");
			if(hp<=0){
				if(message.containsKey("sender")){
					Hashtable<String, Double> message = GameLogic.createSimpleXpMessage(computeXpReward(), this);
					GameLogic.sendMessage(((Hashtable<String, Messageable>)message).get("sender"), message);
				}
			}
		}
	}
}
