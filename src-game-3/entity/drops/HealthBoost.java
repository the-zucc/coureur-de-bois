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

public class HealthBoost extends DroppableFloatingEntity {
	private String entityName;
	private double hpBoost;
	public HealthBoost(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		hpBoost = Math.random() * 50 + 25;
		entityName = hpBoost < 35 ? "minor health boost": hpBoost < 50 ? "medium health boost" : "high efficiency health boost";  
	}

	@Override
	protected boolean canBePickedUp() {
		return true;
	}

	@Override
	protected void onPickup(Object... params) {
		messenger.send("hp", params[1], hpBoost);
		map.removeEntity(this);
	}
	private int ticksToGrow = 8;
	@Override
	public void onHover(MouseEvent me) {
		animator.animate(()->{
			double scale = getComponent().getScaleX();
			getComponent().setScaleX(scale*1.02);
			getComponent().setScaleY(scale*1.02);
			getComponent().setScaleZ(scale*1.02);
		}, ticksToGrow);
	}

	@Override
	public void onUnHover(MouseEvent me) {
		animator.animate(()->{
			double scale = getComponent().getScaleX();
			getComponent().setScaleX(scale/1.02);
			getComponent().setScaleY(scale/1.02);
			getComponent().setScaleZ(scale/1.02);
		}, ticksToGrow);
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
		PhongMaterial material = new PhongMaterial(Color.RED);
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		Box vertical = new Box(meter*0.2,meter*0.6, meter*0.2);
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
