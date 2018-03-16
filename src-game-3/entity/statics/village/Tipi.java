package entity.statics.village;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import util.MeshFactory;
import visual.Component;

import java.util.Hashtable;

public class Tipi extends StaticVisibleCollidingEntity {

    public Tipi(Point3D position) {
        super(position);
    }

    @Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());
        returnVal.getChildren().add(MeshFactory.buildRegularPyramid(6, 175,100));
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
