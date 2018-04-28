package entity;

import app.App;
import characteristic.ComponentOwner;
import characteristic.interactive.Hoverable;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.Positionnable2D;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import visual.Component;

public abstract class VisibleEntity extends Entity implements ComponentOwner{
	
	private Component component;
	protected Point3D position;
	protected Point2D position2D;
	
	public VisibleEntity(Point3D position){
		super();
		setPosition(position);
		component = buildComponent();
		component.setTranslateX(position.getX());
		component.setTranslateY(position.getY());
		component.setTranslateZ(position.getZ());
		component.setCursor(getHoveredCursor());
		component.setOnMouseEntered((e)->{
			this.onHover(e);
			App.getUserInterface().getGameScreen().setMouseTooltipText(getMouseToolTipText());
		});
		component.setOnMouseExited((e)->{
			App.getUserInterface().getGameScreen().setMouseTooltipText("");
			this.onUnHover(e);
		});
	}
	
	protected abstract Cursor getHoveredCursor();
	protected abstract String getMouseToolTipText();
	@Override
	public void setPosition(Point3D position) {
		this.position = position;
		set2DPosition(compute2DPosition(position));
	}

	@Override
	public Point3D getPosition() {
		return this.position;
	}

	@Override
	public abstract Component buildComponent();
	
	@Override
	public Component getComponent(){
		return component;
	};

	@Override
	public boolean isComponentInScene() {
		return getComponent().getParent() != null;
	}

	@Override
	public void placeComponentInScene() {
		Map.getInstance().getComponent().addChildComponent(getComponent());
		getComponent().setTranslateX(getPosition().getX());
		getComponent().setTranslateY(getPosition().getY());
		getComponent().setTranslateZ(getPosition().getZ());
	}
	
	public Point2D compute2DPosition(Point3D position3D){
		return new Point2D(position3D.getX(), position3D.getZ());
	}
	public void set2DPosition(Point2D position2D){
		this.position2D = position2D;
	}
	public Point2D get2DPosition(){
		return position2D;
	}
	public double distanceFrom(Positionnable2D arg0){
		return position2D.distance(arg0.get2DPosition());
	}
	
}
