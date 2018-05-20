package entity.wearable;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.positionnable.Positionnable;
import collision.CollisionBox;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import visual.Component;

public class StandardSword extends WeaponEntity{
	protected double swordLength; 
	public StandardSword(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		swordLength = GameLogic.getMeterLength()*2;
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		Box sword = new Box(0.1*meter, meter,0.1*meter);
		sword.setTranslateY(-sword.getHeight()/2);
		Box trimPiece = new Box(0.1*meter, 0.1*meter, 0.4*meter);
		trimPiece.setTranslateY(-sword.getHeight()/4);
		returnVal.getChildren().addAll(sword, trimPiece);
		return returnVal;
	}
	@Override
	protected String getMouseToolTipText() {
		return "Standard sword";
	}

	@Override
	protected Point3D computeWieldedPosition() {
		return new Point3D(0.4*GameLogic.getMeterLength(),-0.35*GameLogic.getMeterLength(),0);
	}
	private boolean isAttacking = false;
	@Override
	public void attack(MessageReceiver mr){
		Component c = getComponent();
		Point3D attackRotate = Rotate.X_AXIS.add(new Point3D(1.5,5,0));
		if(!this.isAttacking) {
			isAttacking = true;
			animator.animate(()->{
				c.setTranslateZ(c.getTranslateZ()+0.25);
				c.setRotationAxis(attackRotate);
				c.setRotate(c.getRotate()-6);
			}, 8).done(()->{
				if(mr instanceof Positionnable) {
					if(((Positionnable) mr).getPosition().distance(getReceiver().getPosition()) < swordLength){
						System.out.println(swordLength);
						System.out.println(((Positionnable) mr).getPosition().distance(getReceiver().getPosition()));
						super.attack(mr);
					}
				}else{
					System.out.println("attacc");
					super.attack(mr);				
				}
			}).then(()->{
				c.setTranslateZ(c.getTranslateZ()-0.25);
				c.setRotationAxis(attackRotate);
				c.setRotate(c.getRotate()+6);
			}, 8).done(()->{
				StandardSword.this.isAttacking = false;
			});
		}
	}
	public double getSwordLength() {
		return swordLength;
	}
}
