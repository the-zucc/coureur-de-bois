package entity.statics.tree;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
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
import util.NodeUtils;

import java.util.concurrent.ThreadLocalRandom;

public class TreeNormal extends StaticVisibleCollidingEntity {
    public TreeNormal(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
    }

    @Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());

        double meter = GameLogic.getMeterLength();
        Box trunk = new Box(0.6*meter, 2*meter, 0.6*meter);
        Box leaves = new Box(2*meter, 1.8*meter, 2*meter);

        leaves.setTranslateY(-(trunk.getHeight()+leaves.getHeight()/2));
        leaves.setMaterial(new PhongMaterial(Color.LAWNGREEN));

        trunk.setMaterial(new PhongMaterial(Color.SADDLEBROWN));
        trunk.setTranslateY(-trunk.getHeight()/2);

        double value = ThreadLocalRandom.current().nextDouble(1.5)+1;

        returnVal.setScaleX(value);
        returnVal.setScaleY(value);
        returnVal.setScaleZ(value);

        Point3D position = getPosition();

        returnVal.getChildren().addAll(leaves, trunk);
        return returnVal;
    }

    @Override
    public CollisionBox buildCollisionBox() {
        return null;
    }

    @Override
    public void onCollides(Collideable c) {

    }

    @Override
    public Point3D getAllCorrections() {
        return null;
    }

	@Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	public void onHover(MouseEvent me) {
		
	}

	@Override
	public void onUnHover(MouseEvent me) {
		
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
		// TODO Auto-generated method stub
		
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

}
