package entity;

import characteristic.ComponentOwner;
import characteristic.positionnable.Collideable;
import game.Map;
import javafx.geometry.Point3D;
import visual.Component;

public abstract class VisibleEntity extends Entity implements ComponentOwner{
	
	private Component component;
	protected Point3D position;
	
	public VisibleEntity(Point3D position){
		super();
		setPosition(position);
		component = buildComponent();
	}
	
	@Override
	public void setPosition(Point3D position) {
		this.position = position;
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

}
