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
import visual.Component;

public abstract class Entity{
	
	//instance variables
	private String id;
	protected Point3D position;
	protected CollisionBox collisionBox;
	protected Component component;
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
	 * builds the {@link Component} of the object
	 * @return a {@link Component} representing the element's 3D visuals 
	 */
	public abstract Component buildComponent();
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
		if(position.getY() < CollisionGrid.getInstance().getHeightAt(position)){
			if(gravity == null)
				gravity = new Point3D(GameLogic.getGravity().getX(), GameLogic.getGravity().getY(), GameLogic.getGravity().getZ());
			else
				gravity = gravity.add(GameLogic.getGravity());
		}
		if(gravity != null){
			position = position.add(gravity);
		}
		if(position.getY() > CollisionGrid.getInstance().getHeightAt(position)){
			position = new Point3D(position.getX(), CollisionGrid.getInstance().getHeightAt(position), position.getZ());
			gravity = null;
		}
		//System.out.println(CollisionGrid.getInstance().getHeightAt(position));
		//if(this == Model.getInstance().getCurrentPlayer())
			//position = new Point3D(position.getX(), CollisionGrid.getInstance().getHeightAt(position), position.getZ());
	}
	public void update(double deltaTime){
		fall();
		correctCollisions();
	}
}
