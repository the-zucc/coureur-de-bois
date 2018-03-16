package entity.statics.tree;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import util.IdMaker;
import visual.Component;
import javafx.scene.shape.Box;

import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

public class TreeNormal extends StaticVisibleCollidingEntity {
    public TreeNormal(Point3D position) {
        super(position);
    }

    @Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());

        Box trunk = new Box(30, 100, 30);
        Box leaves = new Box(100, 80, 100);

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
    public void onMessageReceived(Hashtable<String, ?> message) {

    }
}
