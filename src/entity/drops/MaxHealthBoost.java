package entity.drops;

import app.App;
import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import visual.Component;

public class MaxHealthBoost extends DroppableFloatingEntity {
	private String entityName;
	private double hpBoost;
	public MaxHealthBoost(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		hpBoost = Math.random() * 50 + 25;
		entityName = hpBoost < 35 ? "minor health upgrade": hpBoost < 50 ? "medium health upgrade" : "high efficiency health upgrade";  
	}

	@Override
	protected boolean canBePickedUp() {
		return true;
	}

	@Override
	protected void onPickup(Object... params) {
		messenger.send("max_hp", params[1], hpBoost);
		messenger.send("remove", this);
	}
	

	

	@Override
	public void onClick(MouseEvent me) {
		messenger.send("right_clicked", this);
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}

	@Override
	protected String getMouseToolTipText() {
		return entityName;
	}

	@Override
	public Component buildComponent() {
		PhongMaterial material = new PhongMaterial(Color.BLUE);
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		Box vertical = new Box(meter*0.2,meter*0.600001/*wtf, 0.6 fonctionne pas. ca fonctionnait avant*/, meter*0.2);
		vertical.setTranslateY(-vertical.getHeight()/2);
		vertical.setMaterial(material);
		Box horizontal = new Box(meter*0.6,meter*0.2, meter*0.2);
		horizontal.setTranslateY(-(horizontal.getHeight()*1.5));
		horizontal.setMaterial(material);
		returnVal.getChildren().addAll(vertical, horizontal);
		return returnVal;
	}

	@Override
	protected Parent buildOnClickedPane() {
		return null;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		// TODO Auto-generated method stub
		return null;
	}

}
