package entity.living.human;

import java.util.ArrayList;

import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import characteristic.interactive.UserControllable;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import visual.Component;

public class Player extends Human implements UserControllable, AttachableReceiver{
	
	private static Point3D jumpVector = new Point3D(0, -20*GameLogic.getMeterLength(), 0);

	public Player(Point3D position, Map map) {
		super(position, map, 1);
	}

	@Override
	protected double computeXpReward() {
		return 42069;
	}

	@Override
	public void updateActions(double secondsPassed){
	}

	@Override
	public void onCollides(Collideable c) {
		
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		Component playerNode = new Component(getId()+"_body");

		double meter = GameLogic.getMeterLength();

		//body boxes
		Box box1 = new Box(0.4*meter,0.4*meter,0.4*meter);
		Box box3 = new Box(0.7*meter, 0.2*meter, 0.2*meter);
		box1.setTranslateY(-box1.getHeight()/2);
		box3.setTranslateY(-box1.getHeight()+box3.getHeight()/2);

		//color for the skin of the player
		PhongMaterial materialHead = new PhongMaterial();
		materialHead.setDiffuseColor(Color.PINK);

		//box for the player head
		Box boxHead = new Box(0.6*meter,0.6*meter,0.6*meter);
		boxHead.setTranslateY(-(boxHead.getHeight()/2+0.1*meter));
		boxHead.setMaterial(materialHead);

		//box for the player's nose
		Box boxNose = new Box(0.1*meter,0.1*meter,0.1*meter);
		boxNose.setTranslateY(-(0.5*meter+boxNose.getHeight()/2));
		boxNose.setTranslateZ(-(boxHead.getDepth()/2+boxNose.getDepth()/2));
		boxNose.setMaterial(materialHead);

		//boxes for the hat
		Box boxHat = new Box(0.8*meter, 0.05*meter, 0.8*meter);
		Box boxHat2 = new Box(0.5*meter, 0.2*meter, 0.6*meter);
		boxHat.setTranslateY(-(boxHead.getHeight()+box3.getHeight()+box1.getHeight()+boxHat.getHeight()/2));
		boxHat2.setTranslateY(-(boxHead.getHeight()+box3.getHeight()+box1.getHeight()+boxHat.getHeight()+boxHat2.getHeight()/2));

		//constructing the player's head
		Component playerHead = new Component(getId()+"_head");
		playerHead.getChildren().addAll(boxHead, boxNose, boxHat, boxHat2);
		
		//material for the hat
		PhongMaterial hatMaterial = new PhongMaterial();
		hatMaterial.setDiffuseColor(Color.BLACK);
		boxHat.setMaterial(hatMaterial);
		boxHat2.setMaterial(hatMaterial);

		//adding children to the returnValue
		playerNode.getChildren().addAll(box1, box3, playerHead);
		returnVal.addChildComponent(playerNode);
		
		return returnVal;
	}

	@Override
	public CollisionBox buildCollisionBox() {
		return new SphericalCollisionBox(20,this, new Point3D(0,-10,0), map);
	}

	@Override
	protected double computeMovementSpeed() {
		double speed = 3*GameLogic.getMeterLength();
		if(isRunning())
			return speed*15;
		return speed;
	}

	@Override
	public void onKeyPressed(KeyEvent ke) {
		// TODO Auto-generated method stub
		keyAction(ke, true);
	}

	@Override
	public void onKeyReleased(KeyEvent ke) {
		keyAction(ke, false);
	}

	@Override
	public void keyAction(KeyEvent ke, boolean keyDown) {
		// TODO Auto-generated method stub
		KeyCode code = ke.getCode();
		if(code.equals(KeyCode.W))
			this.setUp(keyDown);
		else if(code.equals(KeyCode.A))
			this.setLeft(keyDown);
		else if(code.equals(KeyCode.S))
			this.setDown(keyDown);
		else if(code.equals(KeyCode.D))
			this.setRight(keyDown);
		else if(code.equals(KeyCode.SHIFT))
			this.setIsRunning(keyDown);
		else if(code.equals(KeyCode.SPACE))
			this.jump();
		ke.consume();
	}

	@Override
	public void additionalComponentUpdates() {

	}

	@Override
	protected Point3D getJumpVector() {
		return jumpVector;
	}

	@Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	public void updateComponent(){
		Component playerBody = getComponent().getSubComponent(getId()+"_body");
		getComponent().setPosition(getPosition());
		playerBody.setRotationAxis(Rotate.Y_AXIS);
		playerBody.setRotate(computeComponentRotationAngle(rotationAngle));
		additionalComponentUpdates();
	}
}
