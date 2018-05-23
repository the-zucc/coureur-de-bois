package entity.living.human;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import characteristic.positionnable.Positionnable;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
import entity.living.LivingEntity;
import entity.statics.tree.Tree;
import entity.wearable.LongSword;
import entity.wearable.StandardSword;
import entity.wearable.WeaponEntity;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import visual.Component;

import java.util.ArrayList;
import java.util.Hashtable;

public abstract class Human extends LivingEntity implements AttachableReceiver {
	int level;
	WeaponEntity wieldedWeapon;
	protected Hashtable<String, Integer> items = new Hashtable<String, Integer>();
	
	private boolean isDoingAction = false;
	protected void toggleIsDoingAction() {
		isDoingAction = !isDoingAction;
		setUp(false);
		setDown(false);
		setLeft(false);
		setRight(false);
	}
	protected boolean isDoingAction() {
		return isDoingAction;
	}
	
	public Human(Point3D position, Map map, Messenger messenger, int level) {
		super(position, map, messenger);
		this.level = level;
		accept("wield_weapon", (params)->{
			if(params[0] == this){
				if(wieldedWeapon == null) {
					wieldWeapon((WeaponEntity)params[1]);					
				}
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
			a.onDetach(this);
			getAttachables().remove(a);
			getComponent().removeChildComponent(a.getComponent());
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
	
	public void dropWeapon() {
		//messenger.send("dropped", we);
		detach(wieldedWeapon);
		wieldedWeapon = null;
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
	@Override
	protected void onDeath() {
		double meter = GameLogic.getMeterLength();
		messenger.send("drop", Math.random() > 0.5 ? new StandardSword(getPosition().add(new Point3D(meter, 0, meter)), map, messenger) : new LongSword(new Point3D(meter, 0, meter), map, messenger));
	}
	protected boolean isCuttingDownTree = false;
	protected void goCutDownTree(Tree tree) {
		toggleIsDoingAction();
		if(!isCuttingDownTree) {
			isCuttingDownTree = true;
			addUpdate(()->{
				this.startMovingTo(tree.get2DPosition());
			}, ()->{
				return this.distanceFrom(tree) < 2.5*GameLogic.getMeterLength();
			}, ()->{
				animator.animate(()->{
					attack(null, 10);
				}, 120).done(()->{
					messenger.send("cut_down_tree", tree, wieldedWeapon);
					isCuttingDownTree = false;
					toggleIsDoingAction();
				});
			});
		}
	}
	@Override
	public CollisionBox buildCollisionBox() {
		return new SphericalCollisionBox(GameLogic.getMeterLength()*0.6,this, new Point3D(0,0,0), map);
	}
}
