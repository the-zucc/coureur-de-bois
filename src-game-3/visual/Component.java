package visual;

import java.util.Hashtable;

import characteristic.positionnable.Positionnable;
import javafx.geometry.Point3D;
import javafx.scene.Group;

public class Component extends Group implements Positionnable{
	private Point3D position;
	private Hashtable<String, Component> subComponents;
	public Component(String id){
		setId(id);
		subComponents = new Hashtable<String, Component>();
	}
	public void addChildComponent(Component child) {
		if(child.getId() != null){
			getChildren().add(child);
			subComponents.put(child.getId(),child);
		}
	}
	@Override
	public void setPosition(Point3D position) {
		this.setTranslateX(position.getX());
		this.setTranslateY(position.getY());
		this.setTranslateZ(position.getZ());
	}
	@Override
	public Point3D getPosition() {
		return position;
	}
	
}
