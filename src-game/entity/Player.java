package entity;

import java.util.ArrayList;
import java.util.Vector;

import app.Main;
import collision.SphericalCollisionBox;
import collision.CollisionBox;
import javafx.animation.Transition;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Engine;
import model.Model;

public class Player extends GameElement implements Refreshable {
	private static int speed = 175;
	private Group playerNode;
	private Point3D oldVectMovement;
	private Point3D vectMovement;
	private Point3D vectJumpAndGrav;
	private double rotation;
	
	//level, xp and others
	
	private double damageLevel;
	private double xpAmount;
	
	boolean up, down, left, right, isRunning, isJumping, newOrientation;
	
	public Player(Point3D position) {
		super(position);
		up = down = left = right = isRunning = isJumping = newOrientation = false;
		playerNode = buildPlayerNode();
		element3D = new Group();
		element3D.getChildren().add(playerNode);
		collisionBox = new SphericalCollisionBox(position, 15);
		oldVectMovement = null;
		vectJumpAndGrav = new Point3D(0,0,0);
		((Group)Main.getInstance().getSubScene("principal").getRoot()).getChildren().add(element3D);
		
		//defining gameplay features
		//hp, level, xp, xp reward, etc.
		level = 1;
		hp=50;
		damageLevel = 10;
	}
	public void setUp(boolean value) {
		up = value;
		newOrientation = true;
	}
	public void setDown(boolean value) {
		down = value;
		newOrientation = true;
	}
	public void setLeft(boolean value) {
		left = value;
		newOrientation = true;
	}
	public void setRight(boolean value) {
		right = value;
		newOrientation = true;
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
	
	//debug
	
	public Group getElement3D() {
		return element3D;
	}
	public Group getPlayerNode() {
		return playerNode;
	}
	
//	public void updateOrientation() {
//		newOrientation = false;
//		double startAngle = playerNode.getRotate();
//		double finalAngle = Math.toDegrees(Math.atan2(vectMovement.getX(), vectMovement.getZ()));
//		final double deltaAngle = finalAngle-startAngle;
//		if(deltaAngle > 180) {
//			final double finalDeltaAngle = -(360-deltaAngle);
//			Transition animation = new Transition() {
//				{
//					setCycleDuration(Duration.millis(200));
//				}
//
//				@Override
//				protected void interpolate(double frac) {
//					playerNode.setRotationAxis(Rotate.Y_AXIS);
//					playerNode.setRotate(startAngle+finalDeltaAngle*frac);
//				}
//			};
//			animation.play();
//		}
//		else {
//			Transition animation = new Transition() {
//				{
//					setCycleDuration(Duration.millis(200));
//				}
//
//				@Override
//				protected void interpolate(double frac) {
//					playerNode.setRotationAxis(Rotate.Y_AXIS);
//					playerNode.setRotate(startAngle+deltaAngle*frac);
//				}
//			};
//			animation.play();
//		}
//	}
	public void setRotation(double degrees) {
		rotation = degrees;
	}
	public void updateOrientation(){
		playerNode.setRotationAxis(Rotate.Y_AXIS);
		playerNode.setRotate(rotation);
	}
	
	@Override
	public void update(double deltaTime) {
		//debug
		updateOrientation();
		position = position.add(vectJumpAndGrav);
		oldVectMovement = vectMovement;
		vectMovement = new Point3D(0,0,0);
		double angle = rotation;
		double x,y = 0;
		if(up) {
			y = Math.cos(Math.toRadians(angle))*speed;
			x = Math.sin(Math.toRadians(angle))*speed;
			vectMovement = vectMovement.add(new Point3D(x, 0, y));
		}
		if(down) {
			y = -Math.cos(Math.toRadians(angle))*speed;
			x = -Math.sin(Math.toRadians(angle))*speed;
			vectMovement = vectMovement.add(new Point3D(x,0,y));
		}
		if(left) {
			x = -Math.cos(Math.toRadians(angle))*speed;
			y = Math.sin(Math.toRadians(angle))*speed;
			vectMovement = vectMovement.add(new Point3D(x,0,y));
		}
		if(right) {
			x = Math.cos(Math.toRadians(angle))*speed;
			y = -Math.sin(Math.toRadians(angle))*speed;
			vectMovement = vectMovement.add(new Point3D(x,0,y));
		}
		vectMovement = vectMovement.multiply(deltaTime);
		if(isRunning)
			vectMovement = vectMovement.add(vectMovement);
		if(isJumping)
			if(position.getY() > 0) {
				position = new Point3D(position.getX(), 0, position.getZ());
				vectJumpAndGrav = new Point3D(0,0,0);
				isJumping = false;
			}else {
				vectJumpAndGrav = vectJumpAndGrav.add(Engine.getGlobalGravityVector());
			}
		position = position.add(vectMovement);
		//update the position of the collision box
		collisionBox.setPosition(position);
//		try {
//			if(!(!up && !down && !left && !right) && newOrientation)
//				updateOrientation();
//		}catch(NullPointerException npe) {
//			
//		}
		correctCollisions();
		
		((Label)Main.getInstance().getDebugNode("label_position_player")).setText(Math.round(position.getX())+" "+Math.round(position.getY())+" "+Math.round(position.getZ())+"\njumping: "+isJumping);
	}
	
	public void attack(GameElement ge) {
		if(ge.dealDamage(damageLevel))
			ge.getXpReward();
	}
	
	public static Group buildPlayerNode() {
		Group returnVal = new Group();
		Box box1 = new Box(20,20,20);
		Box box2 = new Box(30,30,30);
		Box box3 = new Box(30, 10, 10);
		Box boxNose = new Box(5,5,5);
		
		PhongMaterial materialHead = new PhongMaterial();
		materialHead.setDiffuseColor(Color.PINK);
		box2.setMaterial(materialHead);

		//main boxes
		box1.setTranslateY(-12.5);
		box2.setTranslateY(-35);
		box3.setTranslateY(-10.75);
		boxNose.setTranslateY(-35);
		boxNose.setTranslateZ(17.5);
		
		
		//material for main boxes
		
		//boxes for the hat
		Box boxHat = new Box(40, 2.5, 40);
		Box boxHat2 = new Box(25, 10, 30);
		boxHat.setTranslateY(-51.25);
		boxHat2.setTranslateY(-57.5);
		
		//material for the hat
		PhongMaterial hatMaterial = new PhongMaterial();
		hatMaterial.setDiffuseColor(Color.CYAN);
		boxHat.setMaterial(hatMaterial);
		boxHat2.setMaterial(hatMaterial);
		
		//
		box1.setTranslateY(-12.5);
		//adding children to the returnValue
		returnVal.getChildren().addAll(box1, box2, box3, boxHat, boxHat2, boxNose);
		return returnVal;
	}
	public double getXpReward() {
		return 10 * (level*10 + hp*5);
	}
}
