package entity.living.animal;

import characteristic.Messenger;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
import entity.drops.HealthBoost;
import entity.drops.WoodPiece;
import entity.statics.tree.Tree;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import visual.Component;
import visual.LegComponent;

public class Beaver extends Animal {

	private Tree targetTree;
	private boolean isCuttingDownTree = false;
	private boolean isTransportingWood = false;


	public Beaver(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
	}

	long treeAttackCooldown = 1000;
    long lastTreeAttack;
	protected void goCutDownTree(Tree targetTree) {
		isCuttingDownTree = true;
		this.targetTree = targetTree;
        setTargetEntity(targetTree);
		startMainAction(()->{
            startMovingTo(targetTree.get2DPosition());
        }, ()->{
            return isAtTarget();
        }, ()->{
		    startMainAction(()->{
		        long thisTreeAttack = System.currentTimeMillis();
		        long delay = thisTreeAttack - lastTreeAttack;
		        if(delay > treeAttackCooldown){
		            lastTreeAttack = thisTreeAttack;
		            jump();
                    messenger.send("cut_down_tree_beaver", targetTree);
                }

            }, ()->{
		        if(this.targetTree != null){
                    return this.targetTree.getHealth() <= 0;
                }
		        return false;
		    }, ()->{
                Beaver.this.targetTree = null;
                Beaver.this.isCuttingDownTree = false;
            });
        });
	}

	@Override
	protected double computeSubmissionFactor() {
		return Math.random()/2;
	}
	private double ticks = 0;
	@Override
	public void updateActions(double secondsPassed) {
		if(targetTree != null) {
			if(!isCuttingDownTree) {
				goCutDownTree(targetTree);
			}
		}else {
			if(map.getTrees().size()>0) {
				Tree potentialTarget = null;
				int count = 0;
				while(targetTree == null && count < 10) {
					potentialTarget = map.getTrees().get((int)Math.floor(Math.random()*map.getTrees().size()));
					if(potentialTarget.distance2DFrom(this) < 10*GameLogic.getMeterLength()) {
						targetTree = potentialTarget;
					}
					count++;
				}
				if(targetTree == null) {
					super.updateActions(secondsPassed);
				}
			}else {
				super.updateActions(secondsPassed);
			}
		}
		tail.setRotationAxis(Rotate.X_AXIS);
		ticks+=0.5;
		tail.setRotate(tail.getRotate()+3.5*Math.sin(ticks));
	}

	@Override
	protected String getMouseToolTipText() {
		return "Beaver";
	}
	Box tail;
	@Override
	public Component buildComponent() {
		PhongMaterial material = new PhongMaterial(Color.SADDLEBROWN);
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		Box body = new Box(0.3*meter,0.3*meter,0.8*meter);
		body.setTranslateY(-(0.15*meter));
		body.setMaterial(material);
		double width = 0.25*meter;
		double depth = 0.7*meter;
		double[] boxSize = {0.125*meter, 0.125*meter, 0.125*meter};
		LegComponent[] legs = new LegComponent[4];
		
		legs[0] = new LegComponent("leg0",0,0, 0.1*meter, boxSize, material);
		legs[0].setTranslateX(-width/2);
		legs[0].setTranslateY(-legs[0].getLeg().getHeight()/2);
		legs[0].setTranslateZ(depth/2);

		legs[1] = new LegComponent("leg1",1,0, 0.1*meter, boxSize, material);
		legs[1].setTranslateX(width/2);
		legs[1].setTranslateY(-legs[1].getLeg().getHeight()/2);
		legs[1].setTranslateZ(depth/2);

		legs[2] = new LegComponent("leg2",1,0, 0.1*meter, boxSize, material);
		legs[2].setTranslateX(-width/2);
		legs[2].setTranslateZ(-depth/2);
		legs[2].setTranslateY(-legs[2].getLeg().getHeight()/2);

		legs[3] = new LegComponent("leg3",0,0, 0.1*meter, boxSize, material);
		legs[3].setTranslateX(width/2);
		legs[3].setTranslateZ(-depth/2);
		legs[3].setTranslateY(-legs[3].getLeg().getHeight()/2);
		
		Box head = new Box(0.3*meter, 0.3*meter, 0.3*meter);
		head.setMaterial(material);
		head.setTranslateY(-0.25*meter);
		head.setTranslateZ(0.5*meter);
		
		
		Box headBox2 = new Box(0.15*meter, 0.15*meter, 0.15*meter);
		headBox2.setTranslateZ(head.getBoundsInParent().getMaxZ()+headBox2.getDepth()/2);
		headBox2.setTranslateY(head.getBoundsInParent().getMaxY()-headBox2.getHeight()/2);
		headBox2.setMaterial(material);	
		
		tail = new Box(0.25*meter, 0.05*meter, 0.6*meter);
		tail.setTranslateZ(-depth*0.8);
		tail.setMaterial(new PhongMaterial(new Color(0,0,0,1)));
		returnVal.getChildren().addAll(legs);
		returnVal.getChildren().addAll(body, head, headBox2, tail);
		return returnVal;
	}



	@Override
	public CollisionBox buildCollisionBox() {
		return new SphericalCollisionBox(GameLogic.getMeterLength()/3, this, new Point3D(0,-2.5,0), map);
	}

	@Override
	protected void onDeath() {
		double meter = GameLogic.getMeterLength();
		messenger.send("drop", new HealthBoost(getPosition(), map, messenger));
		messenger.send("drop", new WoodPiece(getPosition().add(new Point3D(Math.random()*meter, 0, Math.random()*meter)), map, messenger));
	}



	@Override
	public void onClick(MouseEvent me) {
		
	}



	@Override
	protected double computeXpReward() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public void additionalComponentUpdates() {
		// TODO Auto-generated method stub
		
	}
}
