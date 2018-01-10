package entity;

import java.util.concurrent.ThreadLocalRandom;

import app.Main;
import collision.CircularCollisionBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import model.Engine;
import model.Model;
import util.IdMaker;

public class DefaultMob extends GameElement implements Refreshable{
	
	private String id;
	
	//3D element
	private Group parent;
	
	//positions and vectors
	private Point3D target;
	private Point3D vectMovement;
	private Point3D vectGravityAndJump;
	
	//variables
	private boolean isJumping;
	private double orientation;
	
	public DefaultMob(Point3D position){
		super(new Point3D(position.getX(), Model.getInstance().getFloorMatrix().getHeightAt(position), position.getZ()));
		
		vectMovement = new Point3D(0,0,0);
		vectGravityAndJump = new Point3D(0,0,0);
		
		element3D = buildElement3D();
		//defining the collision box
		collisionBox = new CircularCollisionBox(position, 15);
		
		parent = (Group)Main.getInstance().getScene("principal").getRoot();
		parent.getChildren().addAll(element3D);
	}
	
	public void targetPoint(Point3D target){
		this.target = target;
		Point3D vect = target.subtract(position).normalize();
		vectMovement = vect;
		updateOrientation();
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
	public void update() {
		Point3D newPosition = position.add(vectMovement).add(vectGravityAndJump);
		while(newPosition.getX() > Model.maxCoordDebug || newPosition.getX() < Model.minCoordDebug || newPosition.getZ() > Model.maxCoordDebug || newPosition.getZ() < Model.minCoordDebug) {
			int min = Model.minCoordDebug;
			int max = Model.maxCoordDebug;
			double x = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			double z = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			Point3D pos = new Point3D(x, 0, z);
			targetPoint(pos);
			newPosition = position.add(vectMovement);
		}
		position = newPosition;
		
		//update the position of the collision box
		collisionBox.setPosition(position);
		correctCollisions();
		element3D.setTranslateX(position.getX());
		element3D.setTranslateY(position.getY());
		element3D.setTranslateZ(position.getZ());
		if(target != null)
			if(position.distance(target) < vectMovement.distance(Point3D.ZERO)) {
				position = target;
				//vectMovement = new Point3D(0,0,0);
				
				int min = Model.minCoordDebug;
				int max = Model.maxCoordDebug;
				double x = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
				double z = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
				Point3D pos = new Point3D(x, 0, z);
				targetPoint(pos);
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
	/**
	 * this function sets the angle of rotation of the element3D object to the specified angle
	 * @param degrees the desired angle of rotation 
	 */
	public void updateOrientation(double degrees) {
		element3D.getTransforms().add(new Rotate(degrees, Rotate.Y_AXIS));
	}
	/**
	 * this function sets the angle of rotation of the element3D object so that it faces the mob's target walking position
	 */
	public void updateOrientation() {
		double angle = Math.toDegrees(Math.atan2(vectMovement.getX(), vectMovement.getZ()));
		element3D.setRotationAxis(Rotate.Y_AXIS);
		element3D.setRotate(angle);
	}
	public boolean isTouchingAMob() {
		Bounds boundsInScene = element3D.localToScene(element3D.getBoundsInLocal());
		for(Refreshable r:Model.getInstance().getRefreshables()){
			if(r != this)
				if(boundsInScene.intersects(((DefaultMob)r).getBounds()))
					return true;
		}
		return false;
	}
	
	public Bounds getBounds() {
		return element3D.localToScene(element3D.getBoundsInLocal());
	}
	
	public void updateJumpVect(){
		if(isJumping){
			//etc
		}
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
		Box boxNose = new Box(5,7,5);
		PhongMaterial materialHead = new PhongMaterial();
		materialHead.setDiffuseColor(Color.PINK);
		box2.setMaterial(materialHead);
		boxNose.setMaterial(materialHead);
		box1.setTranslateY(-12.5);
		box2.setTranslateY(-35);
		box3.setTranslateY(-10.75);
		boxNose.setTranslateY(-35);
		boxNose.setTranslateZ(17.5);
		returnVal.getChildren().addAll(box1, box2, box3, boxNose);
		return returnVal;
	}
}
