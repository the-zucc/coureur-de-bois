package entity.living.human;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
import entity.living.LivingEntity;
import entity.statics.tree.Tree;
import entity.wearable.WeaponEntity;
import game.GameLogic;
import game.Map;
import javafx.application.Platform;
import javafx.geometry.Point3D;
import util.StopCondition;
import visual.Component;

import java.util.ArrayList;
import java.util.Hashtable;

public abstract class Human extends LivingEntity implements AttachableReceiver {
	int level;
	WeaponEntity wieldedWeapon;
	protected Hashtable<String, Integer> items = new Hashtable<String, Integer>();

	private int currentActionIndex;//current action's index in the update queue.


	private boolean isDoingAction = false;
	private Tree targetTree;

	protected void startDoingAction() {
		isDoingAction = true;
		stopMoving();
	}
	protected void stopMoving(){
		setUp(false);
		setDown(false);
		setLeft(false);
		setRight(false);
	}
	protected boolean isDoingAction() {
		return isDoingAction;
	}
	protected void cancelAction(){
		isDoingAction = false;
	}
	
	public Human(Point3D position, Map map, Messenger messenger, int level) {
		super(position, map, messenger);
		this.level = level;
		accept("wield_weapon", (params)->{
			if(params[0] == Human.this){
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
			Platform.runLater(()->{
				((Component) a.getComponent().getParent()).removeChildComponent(a.getComponent());
			});
		}
		Platform.runLater(()->{
			getComponent().addChildComponent(a.getComponent());
		});
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
		
	}
	protected boolean isCuttingDownTree = false;

	long treeAttackCooldown = 1000;
	long lastTreeAttack;
	protected void goCutDownTree(Tree tree) {
		if(this.targetTree != tree){
			this.targetTree = tree;
		}
		startDoingAction();
		startMainAction(()->{
			this.startMovingTo(tree.get2DPosition());
		}, ()->{
			return this.distance2DFrom(tree) < tree.getReachableRadius();
		}, ()->{
			if(!isCuttingDownTree) {
				isCuttingDownTree = true;
			}
			Point3D currPos = getPosition();
			Human.this.startMainAction(()->{
				if(this.isDoingAction()){
					long thisTreeAttack = System.currentTimeMillis();
					long delay = thisTreeAttack - lastTreeAttack;
					if(delay > treeAttackCooldown){
						lastTreeAttack = thisTreeAttack;
						attack(null, 10);
						messenger.send("cut_down_tree_human", tree, wieldedWeapon);
					}
				}
			}, ()->{
				return Human.this.targetTree.getHealth() <= 0;
			}, ()->{
				if(this.isDoingAction()){
					isCuttingDownTree = false;
					cancelAction();
				}
			});
		});

	}

	@Override
	public CollisionBox buildCollisionBox() {
		return new SphericalCollisionBox(GameLogic.getMeterLength()*0.6,this, new Point3D(0,0,0), map);
	}
	protected void attackTree(Tree tree){
		messenger.send("cut_down_tree_human", wieldedWeapon);
	}
}
