package village;

import characteristic.positionnable.Collideable;
import entity.Entity;
import entity.living.human.Player;
import entity.statics.furniture.Table;
import game.GameLogic;
import game.Map;
import game.settings.Preferences;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import visual.Component;

import java.util.ArrayList;

public class HouseMap extends Map {
    protected Point3D spawnPoint;
    public HouseMap() throws Exception{
        updateables = new ArrayList<>();
        entities = new ArrayList<>();
        componentOwners = new ArrayList<>();
        /**
         * setting the map parameters
         */
        this.mapHeight = 100;
        this.mapWidth = 100;
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
        collisionMap = new ArrayList[collisionRows][collisionCols];

        for(int i = 0; i < collisionRows; i++){
            for(int j = 0; j < collisionCols; j++){
                collisionMap[i][j] = new ArrayList<Collideable>();
            }
        }
        spawnPoint = Point3D.ZERO;
        addEntity(new Table(Point3D.ZERO.add(GameLogic.getMeterLength(), 0, GameLogic.getMeterLength()), this, getMessenger()));
    }
    @Override
    public double getHeightAt(Point2D position){
        return 0;
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
    @Override
    public void addEntity(Entity e){
        if(e instanceof Player){
            ((Player) e).setPosition(this.spawnPoint);
        }
        super.addEntity(e);
    }
}
