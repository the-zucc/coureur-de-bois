package entity.statics.tree;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import util.IdMaker;
import visual.Component;
import javafx.scene.shape.Box;

import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

public class TreeNormal extends StaticVisibleCollidingEntity {
    public TreeNormal(Point3D position, Map map) {
        super(position, map);
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
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public void onMessageReceived(Hashtable<String, ? extends Object> message) {
		
	}
}
