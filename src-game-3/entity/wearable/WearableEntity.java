package entity.wearable;

import characteristic.Messenger;
import characteristic.attachable.AttachableReceiver;
import characteristic.positionnable.Collideable;
import characteristic.wearable.Wearable;
import collision.CollisionBox;
import entity.GravityAffectedCollidingEntity;
import entity.item.wearable.WearableItem;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import visual.Component;

public abstract class WearableEntity extends GravityAffectedCollidingEntity implements Wearable {
    private WearableItem item;
    public WearableEntity(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
        item = buildItem();
        accept("wear", (params)->{

        });
    }

    @Override
    public WearableItem getItem() {
        return item;
    }

    @Override
    public abstract WearableItem buildItem();

    @Override
    public abstract void onWearableWorn();

    @Override
    public void onAttach(AttachableReceiver ar) {

    }

    @Override
    public void onDetach(AttachableReceiver ar) {

    }

    @Override
    public AttachableReceiver getReceiver() {
        return null;
    }

    @Override
    public Point3D getPositionRelativeToReceiver() {
        return null;
    }

    @Override
    public Point3D computeAbsolutePosition() {
        return null;
    }

    @Override
    protected Cursor getHoveredCursor() {
        return null;
    }

    @Override
    protected String getMouseToolTipText() {
        return null;
    }

    @Override
    public abstract Component buildComponent();

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
        return null;
    }

    @Override
    public void onCollides(Collideable c) {

    }

    @Override
    public double computeCollidingWeight() {
        return 0;
    }

    @Override
    public boolean shouldUpdate() {
        return false;
    }

    @Override
    public void onClick(MouseEvent me) {

    }

    @Override
    public void onHover(MouseEvent me) {

    }

    @Override
    public void onUnHover(MouseEvent me) {

    }
    @Override
    public void update(double secondsPassed){
        super.update(secondsPassed);
    }
}
