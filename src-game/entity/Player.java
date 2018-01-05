package entity;

import app.Main;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import model.Engine;
import model.Model;

public class Player implements Refreshable {
	private Group element3D;
	private Point3D vectMovement;
	private Point3D position;
	private Point3D vectJumpAndGrav;
	
	boolean up, down, left, right, isRunning, isJumping;
	
	public Player(Point3D position) {
		up = down = left = right = isRunning = isJumping = false;
		this.position = position;
		element3D = buildElement3D();
		vectJumpAndGrav = new Point3D(0,0,0);
		((Group)Main.getInstance().getScene("principal").getRoot()).getChildren().add(element3D);
	}
	public void setUp(boolean value) {
		up = value;
	}
	public void setDown(boolean value) {
		down = value;
	}
	public void setLeft(boolean value) {
		left = value;
	}
	public void setRight(boolean value) {
		right = value;
	}
	public void setIsRunning(boolean value) {
		isRunning = value;
	}
	public void jump() {
		if(!isJumping) {
			isJumping = true;
			vectJumpAndGrav = vectJumpAndGrav.add(Engine.getGlobalJumpVector());
		}
	}
	public Group getElement3D() {
		return element3D;
	}
	
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
	
	@Override
	public void refresh() {
		//debug
		vectMovement = new Point3D(0,0,0);
		if(up)
			vectMovement = vectMovement.add(new Point3D(0,0,2));
		else if(down)
			vectMovement = vectMovement.add(new Point3D(0,0,-2));
		if(left)
			vectMovement = vectMovement.add(new Point3D(-2,0,0));
		else if(right)
			vectMovement = vectMovement.add(new Point3D(2,0,0));
		if(isRunning)
			vectMovement = vectMovement.add(vectMovement);
		if(isJumping)
			if(-(position.getY()-0) <= -(Engine.getGlobalGravityVector().getY()+1) && -(vectJumpAndGrav.getY()) <= 0) {
				position = new Point3D(position.getX(), 0, position.getZ());
				vectJumpAndGrav = new Point3D(0,0,0);
				isJumping = false;
			}else {
				vectJumpAndGrav = vectJumpAndGrav.add(Engine.getGlobalGravityVector());
			}
		position = position.add(vectJumpAndGrav);
		position = position.add(vectMovement);
		
		
		element3D.setTranslateX(position.getX());
		element3D.setTranslateY(position.getY());
		element3D.setTranslateZ(position.getZ());
		//dwAsdsdSystem.out.println(position.getX()+" "+position.getY()+" "+position.getZ());
	}
}
