package entity;

import app.Main;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import model.Engine;
import model.Model;
import util.IdMaker;

public class DefaultMob implements Refreshable{
	private String id;
	//3D element
	private Group element3D;
	private Group parent;
	
	//positions and vectors
	private Point3D position;
	private Point3D target;
	private Point3D vectMovement;
	private Point3D vectGravityAndJump;
	
	//variables
	private boolean isJumping;
	
	public DefaultMob(Point3D position){
		this.position = new Point3D(position.getX(), Model.getInstance().getFloorMatrix().getHeightAt(position), position.getZ());
		vectMovement = new Point3D(0,0,0);
		vectGravityAndJump = new Point3D(0,0,0);
		element3D = buildElement3D();
		parent = (Group)Main.getInstance().getScene("principal").getRoot();
		parent.getChildren().addAll(element3D);
	}
	
	public void targetPoint(Point3D target){
		this.target = target;
		Point3D vect = target.subtract(position).normalize();
		vectMovement = vect;
	}
	
	public void jump(){
		if(!isJumping){
			isJumping = true;
			vectGravityAndJump = Engine.getGlobalJumpVector();
		}
	}
	/**
	 * returns the current object's id
	 * @return id of the current object
	 */
	public String getId() {
		return id;
	}
	
	@Override
	public void refresh() {
		position = position.add(vectMovement).add(vectGravityAndJump);
		element3D.setTranslateX(position.getX());
		element3D.setTranslateY(position.getY());
		element3D.setTranslateZ(position.getZ());
		if(position.distance(target) < vectMovement.distance(Point3D.ZERO)) {
			position = target;
			vectMovement = new Point3D(0,0,0);
		}
	}
	
	/**
	 * this function refreshes the gravity vector, and fixes the height of the mob to the floor height if necessary.
	 */
	public void applyGravityIfPossible(){
		double mapHeight = Model.getInstance().getFloorMatrix().getHeightAt(position);
		if(vectGravityAndJump.getY() > 0){
			//if map height at position is smaller than object height and if (height + gravity) < 0
			if(Math.abs(position.getY()-mapHeight) > Math.abs(vectGravityAndJump.getY())){
				position = position.add(vectGravityAndJump);
				vectGravityAndJump = vectGravityAndJump.add(Engine.getGlobalGravityVector());
			}
			else if(Math.abs(position.getY()-mapHeight) < Math.abs(vectGravityAndJump.getY())){
				position = new Point3D(position.getX(), Model.getInstance().getFloorMatrix().getHeightAt(position), position.getZ());
				vectGravityAndJump = new Point3D(0,0,0);
			}
		}
		else if(isJumping){
			position = position.add(vectGravityAndJump);
			vectGravityAndJump.add(Engine.getGlobalGravityVector());
		}
	}
	public void updateJumpVect(){
		if(isJumping){
			//etc
		}
	}
	
	private void updateElementRootCoordinates(){
		
	}
	
//	public static Group buildElement3D(){
//		Group returnVal = new Group();
//		Box box = new Box(10,10,10);
//		box.setTranslateY(-10);
//		returnVal.getChildren().addAll(box);
//		returnVal.setId(IdMaker.next());
//		return returnVal;
//	}
	public static Group buildElement3D() {
		Group returnVal = new Group();
		Box box1 = new Box(20,20,20);
		Box box2 = new Box(30,30,30);
		Box box3 = new Box(30, 10, 10);
		box1.setTranslateY(-12.5);
		box2.setTranslateY(-35);
		box3.setTranslateY(-10.75);
		returnVal.getChildren().addAll(box1, box2, box3);
		return returnVal;
	}
}
