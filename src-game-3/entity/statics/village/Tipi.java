package entity.statics.village;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import util.MeshFactory;
import visual.Component;

import java.util.Hashtable;

public class Tipi extends StaticVisibleCollidingEntity {

    private double radius;
    public double getRadius(){
        return radius;
    }
    private double height;
    public double getHeight(){
        return height;
    }

    public Tipi(Point3D position, Map map) {
        super(position, map);
        this.radius = 100;
        this.height = 175;
    }

    @Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());
        returnVal.getChildren().add(MeshFactory.buildRegularPyramid(6, 175, 100));
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

	@Override
	public double computeCollidingWeight() {
		// TODO Auto-generated method stub
		return 0;
	}
}
