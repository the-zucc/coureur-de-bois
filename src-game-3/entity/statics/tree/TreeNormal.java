package entity.statics.tree;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import javafx.geometry.Point3D;
import util.IdMaker;
import visual.Component;

import java.util.Hashtable;

public class TreeNormal extends StaticVisibleCollidingEntity {
    public TreeNormal(Point3D position) {
        super(position);
    }

    @Override
    public Component buildComponent() {
        Component returnVal = new Component(IdMaker.next());

        return returnVal;
    }

    @Override
    public void update(double secondsPassed) {

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
