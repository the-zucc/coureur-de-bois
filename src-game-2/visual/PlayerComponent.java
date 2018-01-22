package visual;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import util.Updateable;

public class PlayerComponent extends GameComponent implements Updateable {
	
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
		
		//box for the player's nose
		Box boxNose = new Box(5,5,5);
		boxNose.setTranslateY(-12.5);
		boxNose.setTranslateZ(17.5);
		
		//box for the player head
		Box boxHead = new Box(30,30,30);
		boxHead.setTranslateY(-35);
		PhongMaterial materialHead = new PhongMaterial();
		materialHead.setDiffuseColor(Color.PINK);
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
		
		playerNode.getChildren().addAll(box1, box3, boxHat2, playerHead);
		getChildren().add(playerNode);
	}

	@Override
	public void update(double deltaTime) {
		
	}
	public Group getPlayerNode() {
		return playerNode;
	}
	public Group getPlayerHead(){
		return playerHead;
	}
}
