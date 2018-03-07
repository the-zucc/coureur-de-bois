package entity.living.human;

import java.util.ArrayList;

import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import characteristic.interactive.UserControllable;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import game.GameLogic;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import visual.Component;

public class Player extends Human implements UserControllable, AttachableReceiver{
	
	private static Point3D jumpVector = new Point3D(0, -10*GameLogic.getMeterLength(), 0);

	public Player(Point3D position) {
		super(position);
	}

	@Override
	protected double computeXpReward() {
		return 0;
	}

	@Override
	public void updateActions(double secondsPassed){
		//System.out.println(getPosition().add(movement));
	}

	@Override
	public void onCollides(Collideable c) {
		
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		Component playerNode = new Component(getId()+"_body");
		
		//boxes for the hat
		Box boxHat = new Box(40, 2.5, 40);
		Box boxHat2 = new Box(25, 10, 30);
		boxHat.setTranslateY(-51.25);
		boxHat2.setTranslateY(-57.5);
		
		//color for the skin of the player
		PhongMaterial materialHead = new PhongMaterial();
		materialHead.setDiffuseColor(Color.PINK);
		
		//box for the player's nose
		Box boxNose = new Box(5,5,5);
		boxNose.setTranslateY(-27.5);
		boxNose.setTranslateZ(17.5);
		boxNose.setMaterial(materialHead);
		
		//box for the player head
		Box boxHead = new Box(30,30,30);
		boxHead.setTranslateY(-35);
		boxHead.setMaterial(materialHead);
		
		//constructing the player's head
		Component playerHead = new Component(getId()+"_head");
		playerHead.getChildren().addAll(boxHead, boxNose, boxHat, boxHat2);
		
		
		//material for the hat
		PhongMaterial hatMaterial = new PhongMaterial();
		hatMaterial.setDiffuseColor(Color.BLACK);
		boxHat.setMaterial(hatMaterial);
		boxHat2.setMaterial(hatMaterial);
		
		//body boxes
		Box box1 = new Box(20,20,20);
		Box box3 = new Box(30, 10, 10);
		box1.setTranslateY(-12.5);
		box3.setTranslateY(-10.75);
		
		//adding children to the returnValue
		playerNode.getChildren().addAll(box1, box3, playerHead);
		returnVal.getChildren().add(playerNode);
		
		return returnVal;
	}

	@Override
	public CollisionBox buildCollisionBox() {
		return null;
	}

	@Override
	protected double computeMovementSpeed() {
		double speed = 180;
		if(isRunning())
			return speed*2;
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
	public boolean shouldUpdateComponent() {
		return true;
	}

	@Override
	public void updateComponent() {
		getComponent().setPosition(getPosition());
	}

	@Override
	protected Point3D getJumpVector() {
		return jumpVector;
	}

	@Override
	public void attach(Attachable a) {
		getComponent().addChildComponent(a.getComponent());
		a.onAttach(this);
        onAttachActions(a);
	}

	@Override
	public void detach(Attachable a) {
	    if(getAttachables().contains(a)){
            getAttachables().remove(a);
            getComponent().removeChildComponent(a.getComponent());
            a.onDetach(this);
        }
	}

	@Override
	public void onAttachActions(Attachable a) {

	}

    @Override
    public void onDetachActions(Attachable a) {

    }

    @Override
	public ArrayList<Attachable> getAttachables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateAttachables() {

	}
	
}