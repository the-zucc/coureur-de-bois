package visual;

import app.Model;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import util.Updateable;
import visual.GameComponent;

public class MobComponent extends GameComponent implements Updateable{

	public MobComponent(Color color) {
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
		getChildren().addAll(box1, box2, box3, boxNose);
	}
	
	@Override
	public void update(double deltaTime) {
		Point3D position = Model.getInstance().getElement(getId()).getPosition();
		this.setTranslateX(position.getX());
		this.setTranslateY(position.getY());
		this.setTranslateZ(position.getZ());
	}	
}
