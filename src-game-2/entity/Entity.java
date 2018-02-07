package entity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Vector;

import app.GameLogic;
import app.Model;
import collision.CollisionBox;
import collision.CollisionGrid;
import javafx.geometry.Point3D;
import util.IdMaker;
import visual.GameComponent;

public abstract class Entity{
	
	//instance variables
	private String id;
	protected Point3D position;
	protected CollisionBox collisionBox;
	protected Point3D gravity;
	
	protected Entity(Point3D position) {
		this.position = position;
		id = IdMaker.next();
	}
	/**
	 * returns the Id of the element
	 * @return a {@link String} that represents the id of the current element
	 */
	public String getId() {
		return id;
	}
	/**
	 * builds the {@link GameComponent} of the object
	 * @return a {@link GameComponent} representing the element's 3D visuals 
	 */
	public abstract GameComponent buildComponent();
	/**
	 * returns the position of the element
	 * @return a {@link Point3D} containing the element's position
	 */
	public Point3D getPosition() {
		return position;
	}
	public boolean correctCollisions() {
		boolean returnVal = false;
		int row = collisionBox.getMapDivisionRow();
		int column = collisionBox.getMapDivisionColumn();
		for(int i = row-1; i <= row+1; i++) {
			if(i < CollisionGrid.getInstance().getNumberOfMapDivisionRows()-1 && i >= 0) {
				Vector<ArrayList<CollisionBox>> divisionRow = CollisionGrid.getInstance().getRow(i);
				for(int j = column-1; j <= column+1; j++)
					if(j < CollisionGrid.getInstance().getNumberOfMapDivisionColumns()-1 && j >= 0) {
						ArrayList<CollisionBox> boxesFromDivision = divisionRow.get(j);
						int tryCount = 0;
						while(true)
							try {
								tryCount++;
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
								if(tryCount > 10)
									position = position.add(new Point3D(0,10,0));
							}
					}
			}
		}
		return returnVal;
	}
	public CollisionBox getCollisionBox() {
		return collisionBox;
	}
	protected void updateCollisionGrid(){
		collisionBox.setPosition(position);
	}
	protected void fall(){
		if(position.getY() < 0){
			if(gravity == null)
				gravity = new Point3D(GameLogic.getGravity().getX(), GameLogic.getGravity().getY(), GameLogic.getGravity().getZ());
			else
				gravity = gravity.add(GameLogic.getGravity());
		}
		if(gravity != null){
			position = position.add(gravity);
			if(position.getY() > 0){
				position = new Point3D(position.getX(), 0, position.getZ());
				gravity = null;
			}
		}
		
	}
}
