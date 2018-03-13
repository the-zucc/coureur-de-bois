package entity.statics.village;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import visual.Component;

import java.util.Hashtable;

public class Tipi extends StaticVisibleCollidingEntity {

    public Tipi(Point3D position) {
        super(position);
    }

    @Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());
        returnVal.getChildren().add(buildTipiMesh(6, 175,100));
        return returnVal;
    }
    private MeshView buildTipiMesh(int sides, float negativeHeight, float radius){
        float height = -negativeHeight;
        TriangleMesh mesh = new TriangleMesh();
        double angle = Math.toRadians(360/sides);
        mesh.getPoints().addAll(0, 0, 0);//point 0
        for (int i = 0; i < sides; i++) {//generate the vertices around the shape
            float x = (float)Math.cos(angle*i)*radius;
            float z = (float)Math.sin(angle*i)*radius;
            float y = (float)0;
            mesh.getPoints().addAll(x,y,z);
        }
        mesh.getPoints().addAll(0, height,0);

        mesh.getTexCoords().addAll(0,0);

        for (int i = 1; i < sides; i++) {//generate the faces based on the vertices
            mesh.getFaces().addAll(i,0,i+1,0,sides+1,0);
        }
        mesh.getFaces().addAll(sides,0, 1, 0, sides+1,0);

        MeshView mv = new MeshView(mesh);
        mv.setMaterial(new PhongMaterial(Color.PINK));
        return mv;
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
