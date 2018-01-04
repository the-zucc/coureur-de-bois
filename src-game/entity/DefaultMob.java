package entity;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import model.Engine;
import model.Model;

public class DefaultMob implements Refreshable{
	//3D element
	private Group element3D;
	
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
		element3D = createElementRoot();
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
	
	@Override
	public void refresh() {
		
	}
	/**
	 * this function refreshes the gravity vector, and fixes the height of the mob to the floor height if necessary.
	 */
	public void applyGravityIfPossible(){
		double mapHeight = Model.getInstance().getFloorMatrix().getHeightAt(position);
		//if map height at position is smaller than object height and if (height + gravity) < 0
		if(vectGravityAndJump.getY() > 0){
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
	
	public static Group createElementRoot(){
		Group returnVal = new Group();
		Box box = new Box(20,20,20);
		returnVal.getChildren().addAll(box);
		return returnVal;
	}
}
