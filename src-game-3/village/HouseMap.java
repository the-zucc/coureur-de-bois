package village;

import characteristic.positionnable.Collideable;
import game.Map;
import game.settings.Preferences;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import visual.Component;

import java.util.ArrayList;

public class HouseMap extends Map {
    public HouseMap() throws Exception{
        /**
         * setting the map parameters
         */
        this.mapHeight = Math.random()*10;
        this.mapHeight = Math.random()*10;
        /**
         * visual component
         */
        this.component = buildComponent();
        /**
         * messenger functions
         */
        messenger = createMessenger();
        listenTo(messenger);
        /**
         *
         */
        collideables = new ArrayList<Collideable>();
        collisionCols = (int)mapWidth/collisionMapDivisionWidth;
        collisionRows = (int)mapHeight/collisionMapDivisionHeight;
        collisionMap = new ArrayList[collisionRows][collisionRows];
        for(int i = 0; i < collisionRows; i++){
            for(int j = 0; j < collisionCols; j++){
                collisionMap[i][j] = new ArrayList<Collideable>();
            }
        }
    }
    @Override
    public Component buildComponent() {
        Component returnVal = new Component("id_house_floor");

        TriangleMesh mesh = new TriangleMesh();

        mesh.getPoints().addAll((float)-mapWidth/2, 0, (float)mapHeight/2);
        mesh.getPoints().addAll((float)-mapWidth/2, 0, (float)-mapHeight/2);
        mesh.getPoints().addAll((float)mapWidth/2, 0, (float)mapHeight/2);
        mesh.getPoints().addAll((float)mapWidth/2, 0, (float)-mapHeight/2);

        mesh.getFaces().addAll(0,0,1,0,2,0);
        mesh.getFaces().addAll(3,0,2,0,1,0);

        mesh.getTexCoords().addAll(0,0);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(new PhongMaterial(new Color(0,0,0,1)));
        returnVal.getChildren().add(meshView);
        return returnVal;
    }
}