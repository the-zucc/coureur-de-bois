package visual;

import entity.Entity;
import entity.living.LivingEntity;
import entity.living.human.Player;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.PointLight;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class PlayerComponent extends LivingComponent{
	
	//the player 3D element
	private Group playerNode;
	private Group playerHead;
	
	public PlayerComponent(Color hatColor) {
		playerNode = new Group();
		
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
		playerHead = new Group();
		playerHead.getChildren().addAll(boxHead, boxNose, boxHat, boxHat2);
		
		
		//material for the hat
		PhongMaterial hatMaterial = new PhongMaterial();
		hatMaterial.setDiffuseColor(hatColor);
		boxHat.setMaterial(hatMaterial);
		boxHat2.setMaterial(hatMaterial);
		
		//body boxes
		Box box1 = new Box(20,20,20);
		Box box3 = new Box(30, 10, 10);
		box1.setTranslateY(-12.5);
		box3.setTranslateY(-10.75);
		
		
		
		//adding children to the returnValue
		
		playerNode.getChildren().addAll(box1, box3, playerHead);
		getChildren().add(playerNode);
		/*
		PointLight light = new PointLight(Color.AQUAMARINE);
		light.setTranslateY(-75);
		getChildren().add(light);
		*/
	}
	
	@Override
	public void update(Entity e) {
		if(e instanceof Player) {
			Point3D position = e.getPosition();
			setTranslateX(position.getX());
			setTranslateY(position.getY());
			setTranslateZ(position.getZ());
			playerNode.setRotationAxis(Rotate.Y_AXIS);
			playerNode.setRotate(((LivingEntity) e).getAngleDegrees()-90);
		}
		else
			System.out.println("wrong entity type for component");
	}
	
	public Group getPlayerNode() {
		return playerNode;
	}
	public Group getPlayerHead(){
		return playerHead;
	}
}
