package entity.statics.furniture;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import util.IdMaker;
import visual.Component;

public class Table extends StaticVisibleCollidingEntity {
    public Table(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
    }

    @Override
    protected Cursor getHoveredCursor() {
        return null;
    }

    @Override
    protected String getMouseToolTipText() {
        return "Table";
    }

    @Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());
        Group table = buildTableWithLegs(1.0, 1.0, 0.5, 0.2, new PhongMaterial(Color.BISQUE));
        returnVal.getChildren().add(table);
        return returnVal;
    }
    public static Group buildTableWithLegs(double widthInMeters,
                                           double lengthInMeters,
                                           double heightInMeters,
                                           double thicknessInMeters, Material material){

        double tableTopThickness = thicknessInMeters*GameLogic.getMeterLength();
        double tableTopWidth = widthInMeters*GameLogic.getMeterLength();
        double tableTopLength = lengthInMeters*GameLogic.getMeterLength();
        double tableHeight = heightInMeters*GameLogic.getMeterLength();

        Group returnVal = new Group();

        Box tableTop = new Box(tableTopWidth, tableTopThickness, tableTopLength);
        tableTop.setTranslateY(-(tableHeight+tableTop.getHeight()/2));
        tableTop.setMaterial(material);
        Box[] tableLegs =  new Box[4];
        double tableLegThickness = 0.10*GameLogic.getMeterLength();
        for(int i = 0; i < 4; i++){
            tableLegs[i] = new Box(tableLegThickness, tableHeight, tableLegThickness);
            tableLegs[i].setTranslateY(-(tableHeight/2));
            tableLegs[i].setMaterial(material);
        }
        tableLegs[0].setTranslateX(-(tableTopWidth/2 - tableLegThickness/2));
        tableLegs[0].setTranslateZ(-(tableTopLength/2 - tableLegThickness/2));

        tableLegs[1].setTranslateX((tableTopWidth/2 - tableLegThickness/2));
        tableLegs[1].setTranslateZ((tableTopLength/2 - tableLegThickness/2));

        tableLegs[2].setTranslateX(-(tableTopWidth/2 - tableLegThickness/2));
        tableLegs[2].setTranslateZ((tableTopLength/2 - tableLegThickness/2));

        tableLegs[3].setTranslateX((tableTopWidth/2 - tableLegThickness/2));
        tableLegs[3].setTranslateZ(-(tableTopLength/2 - tableLegThickness/2));

        returnVal.getChildren().addAll(tableTop);
        returnVal.getChildren().addAll(tableLegs);
        return returnVal;
    }

    @Override
    protected Parent buildOnClickedPane() {
        return null;
    }

    @Override
    protected Node getPaneDismissNode(Parent onClickedPane) {
        return null;
    }

    @Override
    public CollisionBox buildCollisionBox() {
        return new SphericalCollisionBox(GameLogic.getMeterLength(), this, Point3D.ZERO, map);
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
        return 0;
    }

    @Override
    public boolean shouldUpdate() {
        return true;
    }

    @Override
    public void onClick(MouseEvent me) {

    }
}
