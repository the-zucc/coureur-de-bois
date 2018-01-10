package entity;

import collision.CollisionBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import model.FloorMatrix;
import model.Model;

public abstract class GameElement {
	//3D element
	protected Group element3D;
	
	private Group parent;
	
	protected Point3D position;
	protected CollisionBox collisionBox;
	
	public GameElement(Point3D position) {
		this.position = position;
	}
	
	public Group getElement3D(){
		return element3D;
	}
	public static Group buildElement3D() {
		Group returnVal = new Group();
		returnVal.getChildren().add(new Box(100,100,100));
		return returnVal;
	}
	public boolean isTouchingAGameElement() {
		Bounds boundsInScene = element3D.localToScene(element3D.getBoundsInLocal());
		for(Refreshable r:Model.getInstance().getRefreshables()){
			if(r != this)
				if(boundsInScene.intersects(((DefaultMob)r).getBounds()))
					return true;
		}
		return false;
	}
	/**
	 * here is the method that is executed every time the model refreshes. it is common to all GameElement subclasses.
	 * Everything added in this method will be applied to all subclasses. 
	 */
	public void refresh() {
		update();
	}
	public abstract void update();
	
	/**
	 * refreshes the FloorMatrix so that
	 */
	public void updateFloorMatrix() {
		
	}
	public Group getParent() {
		return parent;
	}
	public void setParent(Group parent) {
		this.parent = parent;
	}
	public Point3D getPosition() {
		return position;
	}
	public void setPosition(Point3D position) {
		this.position = position;
	}
	public void setElement3D(Group element3d) {
		element3D = element3d;
	}
	public CollisionBox getCollisionBox() {
		return collisionBox;
	}
}
