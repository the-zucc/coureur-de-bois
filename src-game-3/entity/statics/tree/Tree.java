package entity.statics.tree;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.Reachable;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
import entity.drops.HealthBoost;
import entity.drops.WoodPiece;
import entity.living.animal.Beaver;
import entity.statics.StaticVisibleCollidingEntity;
import entity.wearable.LongSword;
import entity.wearable.StandardSword;
import entity.wearable.WoodCuttersAxe;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import visual.Component;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import util.NodeUtils;

import java.util.concurrent.ThreadLocalRandom;

public class Tree extends StaticVisibleCollidingEntity implements Reachable{

	protected double health = 20;
	public double getHealth(){
		return this.health;
	}
	private double reachableRadius;
    public Tree(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
        accept("cut_down_tree_human", (params)->{
        	if(params[0] == this) {
        		if(params[1] != null){
					this.health -= params[1] instanceof WoodCuttersAxe ? 5
							: params[1] instanceof LongSword ? 3
							: params[1] instanceof StandardSword ? 2 : 1;
					System.out.println(this.health);
        		}
        	}
        	if(this.getHealth() <= 0){
				this.getCutDown(true);
			}

        });
        accept("cut_down_tree_beaver", (params)->{
        	if(params[0] == this){
				this.health-=4;
			}
			if(this.getHealth() <= 0){
				this.getCutDown(true);
			}
		});
        this.reachableRadius = computeReachableRadius();

    }

    private double fallingSpeed = 0;
    private void getCutDown(boolean shouldDropWood) {
    	if(fallingSpeed == 0){
			int ticks = 45;
			double fallingAccel = 0.2;
			Component c = getComponent();
			double ang = Math.random()*360;
			Point3D rotationAxis = new Point3D(Math.cos(ang), 0, Math.sin(ang));
			animator.animate(()->{
				fallingSpeed+=fallingAccel;
				c.getTransforms().add(new Rotate(fallingSpeed, rotationAxis));
			}, ticks/2).then(()->{

			}, 20).done(()->{
				messenger.send("remove", this);
				if(shouldDropWood) {
					messenger.send("drop", new WoodPiece(getPosition(), map, messenger));
				}
			});
		}
	}

	protected double treeScale;
    //this method has an effect on variable treeScale. Nothing was found to have a "clean"
	//method that does not have side-effects.
	@Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());

        double meter = GameLogic.getMeterLength();
        Box trunk = new Box(0.6*meter, 2*meter, 0.6*meter);
        Box leaves = new Box(2*meter, 1.8*meter, 2*meter);

        leaves.setTranslateY(-(trunk.getHeight()+leaves.getHeight()/2));
        leaves.setMaterial(new PhongMaterial(Color.WHITE));

        trunk.setMaterial(new PhongMaterial(Color.SADDLEBROWN));
        trunk.setTranslateY(-trunk.getHeight()/2);

        treeScale = ThreadLocalRandom.current().nextDouble(1.5)+1;

        returnVal.setScaleX(treeScale);
        returnVal.setScaleY(treeScale);
        returnVal.setScaleZ(treeScale);

        Point3D position = getPosition();

        returnVal.getChildren().addAll(leaves, trunk);
        return returnVal;
    }

	@Override
    public CollisionBox buildCollisionBox() {
        return new SphericalCollisionBox(GameLogic.getMeterLength(), this, Point3D.ZERO, map);
    }

    @Override
    public void onCollides(Collideable c) {

    }

	@Override
	public Point3D getAllCorrections() {
		return Point3D.ZERO;
	}

	@Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}

	@Override
	protected String getMouseToolTipText() {
		return "Tree";
	}

	@Override
	public void onClick(MouseEvent me) {

	}

	@Override
	protected Parent buildOnClickedPane() {
		Group returnVal = new Group();
		Label l = new Label("No action here");
		l.setId("label");
		returnVal.getChildren().add(l);
		return returnVal;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		
		return NodeUtils.getChildByID(onClickedPane, "label");
	}
	
	@Override
	public boolean shouldUpdate() {
		return true;
	}
	@Override
	public void update(double secondsPassed) {
		super.update(secondsPassed);
	}

	@Override
	public double computeReachableRadius() {
		return this.treeScale * GameLogic.getMeterLength() * 2;
	}

	@Override
	public double getReachableRadius() {
		return this.reachableRadius;
	}
}
