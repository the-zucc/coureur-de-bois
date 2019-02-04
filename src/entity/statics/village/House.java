package entity.statics.village;

import java.io.IOException;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.Reachable;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import util.MeshFactory;
import util.NodeUtils;
import village.HouseMap;
import visual.Component;

public class House extends StaticVisibleCollidingEntity implements Reachable {

	private final float reachableRadius;
	private final float radius;
	public float getRadius(){
		return radius;
	}
	private final float height;
	public float getHeight(){
		return height;
	}
	private HouseMap houseMap;
	public HouseMap getHouseMap(){
		return this.houseMap;
	}

	public House(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		this.radius = 100;
		this.height = 175;
		/*
        accept("reached_entity", (params) -> {
        	if(params[0] == this){
				this.houseMap = buildHouseMap();
				messenger.send("pause_enter_house", this);
			}
		});*/
		this.reachableRadius = computeReachableRadius();
	}
	protected HouseMap buildHouseMap(){
		try{
			return new HouseMap();
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		PhongMaterial houseMaterial = new PhongMaterial(Color.SADDLEBROWN);

		float meter = (float)GameLogic.getMeterLength();
		float houseHeight = 3*meter;
		float houseWidth = 5*meter;
		float houseLength = houseWidth;
		Box houseBase = new Box(houseWidth, houseHeight, houseLength);
		houseBase.setDrawMode(DrawMode.LINE);
		houseBase.setMaterial(houseMaterial);
		returnVal.getChildren().add(houseBase);
		houseBase.setTranslateY(-houseBase.getHeight()/2);
		float roofHeight = 2*meter;
		TriangleMesh roofMesh = new TriangleMesh();
		((TriangleMesh) roofMesh).getPoints().addAll(
				(float)houseBase.getWidth()/2,
				0.0f,
				-(float)houseBase.getDepth()/2,

				0.0f,
				-roofHeight,
				-(float)houseBase.getDepth()/2,

				-(float)houseBase.getWidth()/2,
				0.0f,
				-(float)houseBase.getDepth()/2,


				(float)houseBase.getWidth()/2,
				0.0f,
				(float)houseBase.getDepth()/2,

				0.0f,
				-roofHeight,
				(float)houseBase.getDepth()/2,

				-(float)houseBase.getWidth()/2,
				0.0f,
				(float)houseBase.getDepth()/2

		);
		roofMesh.getFaces().addAll(
				0,0, 1,0, 2,0,//"front" part of the triangular part of the roof
				5,0, 4,0, 3,0,//"rear" part of the triangular part of the roof
				0,0, 3,0, 1,0,//"right" square part, triangle #1
				3,0, 4,0, 1,0,//"right" square part, triangle #2

				2,0, 1,0, 4,0,//"left" square part, triangle #1
				2,0, 4,0, 5,0//"right" square part, triangle #2
		);

		roofMesh.getTexCoords().addAll(0,0);
		MeshView roofMeshView = new MeshView(roofMesh);
		roofMeshView.setDrawMode(DrawMode.LINE);
		roofMeshView.setMaterial(houseMaterial);
		roofMeshView.setTranslateY(-houseBase.getHeight());
		returnVal.getChildren().add(roofMeshView);
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
	public float computeCollidingWeight() {
		return 1;
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}

	@Override
	protected String getMouseToolTipText() {
		return "house";
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

	@Override
	public float computeReachableRadius() {
		return 6*GameLogic.getMeterLength();
	}

	@Override
	public float getReachableRadius() {
		return this.reachableRadius;
	}
}
