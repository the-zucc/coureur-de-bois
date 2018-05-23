package entity.wearable;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.positionnable.Positionnable;
import collision.CollisionBox;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
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
		PhongMaterial trimPieceMaterial = new PhongMaterial(Color.BROWN);
        PhongMaterial handleMaterial = new PhongMaterial(Color.BLACK);
        Component returnVal = new Component(getId());
        double meter = GameLogic.getMeterLength();
        Box sword = new Box(0.1*meter, meter,0.2*meter);
        Box handle = new Box(0.101*meter, 0.4*meter, 0.1*meter);
        handle.setTranslateY(-handle.getHeight()/2);
        sword.setTranslateY(-handle.getHeight()-sword.getHeight()/2);
        Box trimPiece = new Box(0.102*meter, 0.1*meter, 0.45*meter);
        trimPiece.setTranslateY(-handle.getHeight());
        trimPiece.setMaterial(trimPieceMaterial);
        handle.setMaterial(handleMaterial);
        Box swordTip = new Box(0.1*meter, 0.05*meter, sword.getDepth()/2);
        swordTip.setTranslateY(sword.getTranslateY()-sword.getHeight()/2-swordTip.getHeight()/2);
        returnVal.getChildren().addAll(handle, sword, trimPiece, swordTip);
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
					super.attack(mr);
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
