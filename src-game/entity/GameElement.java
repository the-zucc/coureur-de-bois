package entity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Vector;

import collision.CollisionBox;
import collision.CollisionMatrix;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import model.Model;
import util.IdMaker;

public abstract class GameElement {
	//3D element
	protected Group element3D;
	
	protected String id;
	
	protected Group parent;
	
	protected Point3D position;
	
	protected CollisionBox collisionBox;
	
	protected double hp;
	
	protected static double xpReward = 0;
	
	protected int level = 0;
	
	public String getId() {
		return id;
	}
	
	public GameElement(Point3D position) {
		hp = -1;
		id = IdMaker.next();
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
	/**
	 * here is the method that is executed every time the model refreshes. it is common to all GameElement subclasses.
	 * Everything added in this method will be applied to all subclasses. 
	 */
	public void refresh(double deltaTime) {
		update(deltaTime);
	}
	public abstract void update(double deltaTime);
	
	public void updateElement3DPosition() {
		element3D.setTranslateX(position.getX());
		element3D.setTranslateY(position.getY());
		element3D.setTranslateZ(position.getZ());
	}
	
	/**
	 * refreshes the FloorMatrix so that
	 */
	public void updateFloorMatrix() {
		
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
	public boolean correctCollisions() {
		boolean returnVal = false;
		int row = collisionBox.getMapDivisionRow();
		int column = collisionBox.getMapDivisionColumn();
		for(int i = row-1; i <= row+1; i++) {
			if(i < Model.getInstance().getFloorMatrix().getNumberOfMapDivisionRows()-1 && i >= 0) {
				Vector<ArrayList<CollisionBox>> divisionRow = Model.getInstance().getFloorMatrix().getRow(i);
				for(int j = column-1; j <= column+1; j++)
					if(j < Model.getInstance().getFloorMatrix().getNumberOfMapDivisionColumns()-1 && j >= 0) {
						ArrayList<CollisionBox> boxesFromDivision = divisionRow.get(j);
						while(true)
							try {
								for(CollisionBox cb:boxesFromDivision)
									if(cb != collisionBox) {
										if(collisionBox.collides(cb)) {
											returnVal = true;
											position = position.add(collisionBox.getCorrection(cb));
											collisionBox.setPosition(position);
										}
									}
								break;
							}catch(ConcurrentModificationException cme) {
								
							}
					}
			}
		}
		return returnVal;
	}
	/**
	 * deals damage to the element.
	 * @param damage the amount of damage to deal to the element in HP
	 * @return if the element's HP < 0 after the damage is dealt.
	 */
	public boolean dealDamage(double damage) {
		if(hp != -1) {//this is to check if the element's HP has been defined externally
			hp-=damage;
			if(hp <=0) {//this would indicate that the mob is dead
				delete();
				try {
					finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
	private void delete() {
		Model.getInstance().deleteGameElement(this);
	}
	public double getXpReward() {
		return xpReward;
	}
}
