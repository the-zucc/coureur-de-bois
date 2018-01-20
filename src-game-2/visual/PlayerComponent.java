package visual;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import util.Updateable;

public class PlayerComponent extends GameComponent implements Updateable {
	
	//the player 3D element
	private Group playerNode;
	
	public PlayerComponent(Color hatColor) {
		playerNode = new Group();
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
		hatMaterial.setDiffuseColor(hatColor);
		boxHat.setMaterial(hatMaterial);
		boxHat2.setMaterial(hatMaterial);
		
		//
		box1.setTranslateY(-12.5);
		//adding children to the returnValue
		playerNode.getChildren().addAll(box1, box2, box3, boxHat, boxHat2, boxNose);
		getChildren().add(playerNode);
	}

	@Override
	public void update(double deltaTime) {
		
	}
	public Group getPlayerNode() {
		return playerNode;
	}
}
