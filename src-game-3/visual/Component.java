package visual;

import java.util.Hashtable;

import characteristic.positionnable.Positionnable;
import characteristic.positionnable.Positionnable2D;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;

public class Component extends Group implements Positionnable{
	private Point3D position;
	private Point2D position2D;
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
	public void removeChildComponent(Component child){
		getChildren().remove(child);
		subComponents.remove(child.getId());
	}
	public Component getSubComponent(String key){
		return subComponents.get(key);
	}

	@Override
	public void setPosition(Point3D position) {
		this.position = position;
		this.position2D = compute2DPosition(position);
		this.setTranslateX(position.getX());
		this.setTranslateY(position.getY());
		this.setTranslateZ(position.getZ());
	}
	@Override
	public Point3D getPosition() {
		return position;
	}
	@Override
	public Point2D compute2DPosition(Point3D position3d) {
		
		return new Point2D(position3d.getX(), position3d.getZ());
	}
	@Override
	public void set2DPosition(Point2D position2d) {
		this.position2D = position2d;
	}
	@Override
	public Point2D get2DPosition() {
		return position2D;
	}
	@Override
	public double distanceFrom(Positionnable2D arg0) {
		return arg0.get2DPosition().distance(position2D);
	}
	
}
