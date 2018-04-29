package entity.statics.village;

import java.io.IOException;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import game.Map;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import util.MeshFactory;
import util.NodeUtils;
import visual.Component;

public class Tipi extends StaticVisibleCollidingEntity {

    private double radius;
    public double getRadius(){
        return radius;
    }
    private double height;
    public double getHeight(){
        return height;
    }

    public Tipi(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
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
	public double computeCollidingWeight() {
		return 0;
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}

	@Override
	public void onHover(MouseEvent me) {
		
	}

	@Override
	public void onUnHover(MouseEvent me) {
		
	}

	@Override
	protected String getMouseToolTipText() {
		return "tipi";
	}

	@Override
	public void onClick(MouseEvent me) {
		
	}

	@Override
	protected Parent buildOnClickedPane() {
		try {
			Parent returnVal = FXMLLoader.load(getClass().getResource("/fxml/entity_pane.fxml"));
			((Label)(NodeUtils.getChildByID(returnVal, "hpLabel"))).setText("NA");
			return returnVal;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		return onClickedPane;
	}
}
