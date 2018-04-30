package entity.living.human;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import entity.living.LivingEntity;
import entity.wearable.WeaponEntity;
import game.Map;
import javafx.geometry.Point3D;
import visual.Component;

import java.util.ArrayList;

public abstract class Human extends LivingEntity implements AttachableReceiver {
	int level;
	WeaponEntity wieldedWeapon;

	public Human(Point3D position, Map map, Messenger messenger, int level) {
		super(position, map, messenger);
		this.level = level;
		accept("wield_weapon", (params)->{
			if(params[0] == this){
				wieldWeapon((WeaponEntity)params[1]);
			}
		});
	}
	@Override
	public boolean shouldUpdateComponent() {
		return true;
	}
	
	@Override
	public void attach(Attachable a) {
		if(a.getComponent().getParent() instanceof Component){
			((Component) a.getComponent().getParent()).removeChildComponent(a.getComponent());
		}
		getComponent().addChildComponent(a.getComponent());
		a.onAttach(this);
		getAttachables().add(a);
		onAttachActions(a);
	}

	@Override
	public void detach(Attachable a) {
		if(getAttachables().contains(a)){
			getAttachables().remove(a);
			getComponent().removeChildComponent(a.getComponent());
			a.onDetach(this);
		}
	}

	@Override
	public void onAttachActions(Attachable a) {
		
	}

	@Override
	public void onDetachActions(Attachable a) {
		
	}
	private ArrayList<Attachable> attachables = new ArrayList<Attachable>();
	@Override
	public ArrayList<Attachable> getAttachables() {
		return attachables;
	}

	@Override
	public void updateAttachables() {

	}
	
	public void wieldWeapon(WeaponEntity we){
		wieldedWeapon = we;
		messenger.send("wielded", we);
		attach(we);
	}
	@Override
	protected void attack(MessageReceiver target, double damage) {
		if(wieldedWeapon != null){
			wieldedWeapon.attack(target);
		}
		else{
			super.attack(target, damage);
		}
	}
}
