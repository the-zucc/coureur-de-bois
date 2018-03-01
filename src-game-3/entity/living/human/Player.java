package entity.living.human;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import visual.Component;

public class Player extends Human {

	public Player(Point3D position) {
		super(position);
	}

	@Override
	protected double computeXpReward() {
		return 0;
	}

	@Override
	public void update(double secondsPassed) {
		
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
		double speed = 6; 
		if(isRunning())
			return speed*2;
		return speed;
	}
}
