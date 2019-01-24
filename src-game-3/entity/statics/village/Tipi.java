package entity.statics.village;

import java.io.IOException;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import game.GameLogic;
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
import village.HouseMap;
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
    private HouseMap houseMap;

    public Tipi(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
        this.radius = 100;
        this.height = 175;
        accept("enter_house", (params) -> {
        	this.houseMap = buildHouseMap();
        	messenger.send("pause_enter_house", this);
		});
    }
    protected HouseMap buildHouseMap(){
    	try{
			HouseMap map = new HouseMap();
		}catch(Exception e){

		}
    	return null;//TODO remove this asap
	}
    @Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());
        returnVal.getChildren().add(MeshFactory.buildRegularPyramid(6, (float)(3.5*GameLogic.getMeterLength()), (float)(2*GameLogic.getMeterLength())));
        return returnVal;
    }

    @Override
    public CollisionBox buildCollisionBox() {
        return new SphericalCollisionBox(GameLogic.getMeterLength()*2, this, Point3D.ZERO, this.map);
    }

    @Override
    public void onCollides(Collideable c) {

    }

    @Override
    public Point3D getAllCorrections() {
        return Point3D.ZERO;
    }

    @Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
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

	@Override
	public boolean shouldUpdate() {
		return true;
	}
}
