package visual;

import app.Model;
import app.UI;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import util.Updateable;

public class MobComponent extends LivingComponent implements Updateable{

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
		
		//hair
		PhongMaterial materialHair = new PhongMaterial(Color.ORANGERED);
		Box boxHair = new Box(34, 5, 34);
		//Box boxHair2 = new Box(5, )
		boxHair.setMaterial(materialHair);
		boxHair.setTranslateY(-50);
		
		getChildren().addAll(box1, box2, box3, boxNose, boxHair);
		this.setOnMouseEntered(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				UI.getInstance().addUiNode(new FloatingUINode(Model.getInstance().getEntity(getId())));
			}
			
		});
		this.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				UI.getInstance().removeUiNode(getId()+"_info");
			}
		});
	}
	
	@Override
	public void update(double deltaTime) {
		Point3D position = Model.getInstance().getEntity(getId()).getPosition();
		this.setTranslateX(position.getX());
		this.setTranslateY(position.getY());
		this.setTranslateZ(position.getZ());
	}
	public static FloatingUINode buildInformationPane(MobComponent mob) {
		return null;
	}
}
