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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import visual.Component;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import util.NodeUtils;

import java.util.concurrent.ThreadLocalRandom;

public class Tree extends StaticVisibleCollidingEntity implements Reachable{

	protected float health = 20;
	public double getHealth(){
		return this.health;
	}
	private float reachableRadius;
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
                if(this.getHealth() <= 0){
                    this.getCutDown(true);
                }
			}
		});
        this.reachableRadius = computeReachableRadius();

    }

    private float fallingSpeed = 0;
    private void getCutDown(boolean shouldDropWood) {
    	if(fallingSpeed == 0){
			int ticks = 45;
			float fallingAccel = 0.2f;
			Component c = getComponent();
			float ang = (float)Math.random()*360;
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

        float meter = GameLogic.getMeterLength();
        Box trunk = new Box(0.6*meter, 2*meter, 0.6*meter);
        TriangleMesh leaves = new TriangleMesh();
        float leavesWidth = 2*meter;
        float leavesLength = leavesWidth;
        float leavesHeight = 1.8f*meter;
        float halfWidth = leavesWidth/2;
        float halfHeight = leavesHeight/2;
        float halfLength = leavesLength/2;
        leaves.getPoints().addAll(
        		-halfWidth, halfHeight, -halfLength,//front left bottom
				halfWidth, halfHeight, -halfLength,//front right bottom
				halfWidth, -halfHeight, -halfLength,//front right top
				-halfWidth, -halfHeight, -halfLength,//front left top
				halfWidth, halfHeight, halfLength,//rear right bottom
				-halfWidth, halfHeight, halfLength,//rear left bottom
				-halfWidth, -halfHeight, halfLength,//rear left top
				halfWidth, -halfHeight, halfLength//rear right top
		);
        leaves.getTexCoords().addAll(
        		0.0f, 0.0f,
				1.0f, 0.0f,
				1.0f, 1.0f,
				0.0f, 1.0f
		);
        leaves.getFaces().addAll(
        		//front face
        		0,0, 1,1, 2,2,
				2,2, 3,3, 0,0,
				//right face
				1,0, 4,1, 7,2,
				7,2, 2,3, 1,0,
				//rear face
				4,0, 5,1, 6,2,
				6,2, 7,3, 4,0,
				//left face
				5,0, 0,1, 3,2,
				3,2, 6,3, 5,0,
				//top face
				3,0, 2,1, 7,2,
				7,2, 6,3, 3,0
		);
        leaves.getTexCoords().addAll(0.0f, 0.0f);
        //Box leaves = new Box(leavesWidth, 1.8*meter, 2*meter);
		MeshView leavesMeshView = new MeshView(leaves);

        leavesMeshView.setTranslateY(-(trunk.getHeight()+halfHeight));

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image("res/leaves-cartoon-2.png"));
        leavesMeshView.setMaterial(material);

        trunk.setMaterial(new PhongMaterial(Color.SADDLEBROWN));
        trunk.setTranslateY(-trunk.getHeight()/2);

        treeScale = ThreadLocalRandom.current().nextDouble(1.5)+1;

        returnVal.setScaleX(treeScale);
        returnVal.setScaleY(treeScale);
        returnVal.setScaleZ(treeScale);

        Point3D position = getPosition();

        returnVal.getChildren().addAll(leavesMeshView, trunk);
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
	public float computeCollidingWeight() {
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
	public void update(float secondsPassed) {
		super.update(secondsPassed);
	}

	@Override
	public float computeReachableRadius() {
		return (float)this.treeScale * GameLogic.getMeterLength() * 2;
	}

	@Override
	public float getReachableRadius() {
		return this.reachableRadius;
	}
}
