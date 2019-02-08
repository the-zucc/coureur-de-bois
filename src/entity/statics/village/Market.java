package entity.statics.village;

import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Font;
import visual.Component;

public class Market extends House {
    private static String logo = getLogoURL();
    private static Image image = new Image(getLogoURL());
    protected static String getLogoURL() {
        return "res/carrot.png";
    }

    public Market(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
    }
    public Component buildComponent(){
        Component returnVal = super.buildComponent();
        Group signContainer = new Group();
        TriangleMesh signMesh = new TriangleMesh();
        float meter = GameLogic.getMeterLength();
        float signWidth = meter;
        float signHeight = 0.75f*meter;
        float signDepth = 0.1f*meter;
        float halfWidth = signWidth/2;
        float halfHeight = signHeight/2;
        float halfDepth = signDepth/2;
        signMesh.getPoints().addAll(
                -halfWidth, halfHeight, -halfDepth,//vertex 0
                halfWidth, halfHeight,-halfDepth,//vertex 1
                halfWidth, -halfHeight,-halfDepth,//2
                -halfWidth, -halfHeight, -halfDepth,//3

                -halfWidth, halfHeight, halfDepth,//4
                -halfWidth, -halfHeight, halfDepth,//5
                halfWidth, -halfHeight, halfDepth,//6
                halfWidth, halfHeight, halfDepth//7
        );
        signMesh.getTexCoords().addAll(
                0.0f,1.0f,//coord on the texture image for vertex 0
                1.0f,1.0f,//coord on the texture image for vertex 1
                1.0f,0.0f,//2
                0.0f,0.0f,//3

                1.0f,0.0f,//4
                1.0f,1.0f,//5
                0.0f,1.0f,//6
                0.0f,0.0f//7
                );
        signMesh.getFaces().addAll(
                0,0, 1,1, 2,2,
                2,2, 3,3, 0,0,

                4,0, 5,0, 6,0,
                6,0, 7,0, 4,0,

                1,0, 7,0, 6,0,
                6,0, 2,0, 1,0,

                3,0, 2,0, 6,0,
                6,0, 5,0, 3,0,

                0,0, 3,0, 5,0,
                5,0, 4,0, 0,0,

                7,0, 1,0, 0,0,
                0,0, 4,0, 7,0
                );
        MeshView mv = new MeshView(signMesh);
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(image);
        mv.setMaterial(material);
        Box sign = new Box(signWidth, signHeight, signDepth);

        sign.setMaterial(new PhongMaterial(Color.SADDLEBROWN));

        signContainer.getChildren().addAll(sign, mv);
        signContainer.setTranslateX(-3*meter);
        signContainer.setTranslateY(-2*meter);
        returnVal.getChildren().add(signContainer);
        return returnVal;
    }
}
