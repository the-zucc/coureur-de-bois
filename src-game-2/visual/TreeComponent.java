package visual;

import java.util.concurrent.ThreadLocalRandom;

import entity.statics.Tree;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class TreeComponent extends Component {
	public TreeComponent(Tree tree) {
		Box trunk = new Box(30, 100, 30);
		Box leaves = new Box(100, 80, 100);
		leaves.setTranslateY(-(trunk.getHeight()+leaves.getHeight()/2));
		leaves.setMaterial(new PhongMaterial(Color.LAWNGREEN));
		trunk.setMaterial(new PhongMaterial(Color.SADDLEBROWN));
		trunk.setTranslateY(-trunk.getHeight()/2);
		getChildren().addAll(trunk, leaves);
		double value = ThreadLocalRandom.current().nextDouble(1.5)+1;
		setScaleX(value);
		setScaleY(value);
		setScaleZ(value);
		Point3D position = tree.getPosition();
		
		setTranslateY(position.getY()-getBoundsInLocal().getHeight()/2);
		setTranslateX(position.getX());
		setTranslateZ(position.getZ());
	}
}
