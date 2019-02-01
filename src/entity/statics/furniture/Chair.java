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
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import util.Direction;
import visual.Component;

public class Chair extends StaticVisibleCollidingEntity {
    private Direction direction;
    public Chair(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
        this.direction = Direction.FACING_NORTH;
    }

    public Chair rotate(Direction direction){
        this.direction = direction;
        Component component = getComponent();
        component.setRotationAxis(Rotate.Y_AXIS);
        switch (direction){
            case FACING_EAST:
                component.setRotate(90);
                break;
            case FACING_WEST:
                component.setRotate(-90);
                break;
            case FACING_SOUTH:
                component.setRotate(180);
                break;
            case FACING_NORTH:
                component.setRotate(0);
                break;
        }
        return this;
    }


    @Override
    protected Cursor getHoveredCursor() {
        return null;
    }

    @Override
    protected String getMouseToolTipText() {
        return "Chair";
    }

    @Override
    public Component buildComponent() {
        double meter = GameLogic.getMeterLength();
        Component returnVal = new Component(getId());
        Group chairBottom = Table.buildTableWithLegs(0.5, 0.5, 0.4, 0.1, new PhongMaterial(Color.BISQUE));
        returnVal.getChildren().add(chairBottom);
        Box chairBack = new Box(0.5*meter, 0.5*meter, 0.1*meter);
        chairBack.setTranslateY(-(0.5+0.25)*meter);
        chairBack.setTranslateZ(-(0.25)*meter);
        returnVal.getChildren().add(chairBack);
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
        return new SphericalCollisionBox(0.25, this, Point3D.ZERO.add(0,-0.25*GameLogic.getMeterLength(), 0), this.map);
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
