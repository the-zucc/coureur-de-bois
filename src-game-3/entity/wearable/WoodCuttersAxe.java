package entity.wearable;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.positionnable.Positionnable;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import visual.Component;

public class WoodCuttersAxe extends WeaponEntity{
	
	public WoodCuttersAxe(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
	}

	@Override
	protected Point3D computeWieldedPosition() {
		return new Point3D(0.4*GameLogic.getMeterLength(),-0.35*GameLogic.getMeterLength(),0);
	}

	@Override
	protected String getMouseToolTipText() {
		return "Woodcutter's axe";
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		PhongMaterial woodMaterial = new PhongMaterial(Color.BROWN);
		
		Box woodPiece = new Box(0.1*meter, meter, 0.1*meter);
		woodPiece.setTranslateY(-woodPiece.getHeight()/2);
		woodPiece.setMaterial(woodMaterial);
		
		Box metalPiece1 = new Box(0.11*meter, 0.35*meter, 0.35*meter);
		metalPiece1.setTranslateY(-woodPiece.getHeight()+(metalPiece1.getHeight()/2)*1.01);
		metalPiece1.setTranslateZ(-woodPiece.getDepth()+(metalPiece1.getDepth()/2)*1.1);
		
		Box metalPiece2 = new Box(0.05*meter, 0.5*meter, 0.2*meter);
		metalPiece2.setTranslateY(-woodPiece.getHeight()+(metalPiece1.getHeight()/2)*1.01);
		metalPiece2.setTranslateZ(-woodPiece.getDepth()+(metalPiece1.getDepth()*1.005)+metalPiece2.getDepth()/2);
		returnVal.getChildren().addAll(woodPiece, metalPiece1, metalPiece2);
		return returnVal;
	}
	private boolean isAttacking = false;
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
				isAttacking = false;
			});
		}
	}
}
