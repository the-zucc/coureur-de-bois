package entity;

import app.Main;
import javafx.animation.Transition;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Engine;
import model.Model;

public class Player extends GameElement implements Refreshable {
	private Group playerNode;
	private Point3D oldVectMovement;
	private Point3D vectMovement;
	private Point3D vectJumpAndGrav;
	
	boolean up, down, left, right, isRunning, isJumping;
	
	public Player(Point3D position) {
		super(position);
		up = down = left = right = isRunning = isJumping = false;
		playerNode = buildPlayerNode();
		element3D = new Group();
		element3D.getChildren().add(playerNode);
		oldVectMovement = null;
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
	public boolean isTouchingTheFloor() {
		//[minX:-20.0, minY:-62.5, minZ:-20.0, width:40.0, height:65.0, depth:40.0, maxX:20.0, maxY:2.5, maxZ:20.0]
		
		Bounds boundsInScene = playerNode.localToScene(playerNode.getBoundsInLocal());
		
		return boundsInScene.intersects(Model.getInstance().getFloor().getBoundsInLocal());
	}
	//debug
	public boolean isTouchingAMob() {
		Bounds boundsInScene = playerNode.localToScene(playerNode.getBoundsInLocal());
		for(Refreshable r:Model.getInstance().getRefreshables()){
			if(boundsInScene.intersects(((DefaultMob)r).getBounds()))
				return true;
		}
		return false;
	}
	
	public Group getElement3D() {
		return element3D;
	}
	
	public void updateOrientation() {
		double angle = Math.toDegrees(Math.atan2(vectMovement.getX(), vectMovement.getZ()));
		Transition animation = new Transition() {
			{
				setCycleDuration(Duration.millis(100));
			}

			@Override
			protected void interpolate(double frac) {
				playerNode.setRotationAxis(Rotate.Y_AXIS);
				playerNode.setRotate(angle*frac);
			}
		};
		animation.play();
		
	}
	
	
	@Override
	public void refresh() {
		//debug
		oldVectMovement = vectMovement;
		vectMovement = new Point3D(0,0,0);
		position = position.add(vectJumpAndGrav);
		if(up)
			vectMovement = vectMovement.add(new Point3D(0,0,4));
		else if(down)
			vectMovement = vectMovement.add(new Point3D(0,0,-4));
		if(left)
			vectMovement = vectMovement.add(new Point3D(-4,0,0));
		else if(right)
			vectMovement = vectMovement.add(new Point3D(4,0,0));
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
		try {
			if(!vectMovement.normalize().equals(oldVectMovement.normalize()))
				updateOrientation();
		}catch(NullPointerException npe) {
			
		}
		
		
		element3D.setTranslateX(position.getX());
		element3D.setTranslateY(position.getY());
		element3D.setTranslateZ(position.getZ());
		System.out.println(isTouchingAMob());
		//dwAsdsdSystem.out.println(position.getX()+" "+position.getY()+" "+position.getZ());
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
}
