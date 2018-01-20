package entity;

import javafx.geometry.Point3D;
import util.IdMaker;
import util.Updateable;
import visual.GameComponent;

public abstract class GameElement{
	
	//instance variables
	private String id;
	protected Point3D position;
	
	protected GameElement(Point3D position) {
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
}
