package entity;

import java.util.concurrent.ThreadLocalRandom;

import app.Main;
import collision.CylindricalCollisionBox;
import collision.SphericalCollisionBox;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Tree extends GameElement{
	double trunkRadius=30;
	double trunkHeight=100;
	
	public Tree(Point3D position) {
		super(position);
		element3D = buildElement3D(this);
		parent = (Group)Main.getInstance().getSubScene("principal").getRoot();
		parent.getChildren().addAll(element3D);
		updateElement3DPosition();
	}
	public static Group buildElement3D(Tree tree) {
		Group element3D = new Group();
		Box trunk = new Box(30, 100, 30);
		Box leaves = new Box(100, 80, 100);
		leaves.setTranslateY(-(trunk.getHeight()+leaves.getHeight()/2));
		leaves.setMaterial(new PhongMaterial(Color.LAWNGREEN));
		trunk.setMaterial(new PhongMaterial(Color.SADDLEBROWN));
		trunk.setTranslateY(-trunk.getHeight()/2);
		element3D.getChildren().addAll(trunk, leaves);
		double value = ThreadLocalRandom.current().nextDouble(1.5)+1;
		if(tree != null) {
			tree.trunkRadius*=value;
			tree.trunkHeight*=value;
		}
		element3D.setScaleX(value);
		element3D.setScaleY(value);
		element3D.setScaleZ(value);
		element3D.setTranslateY(-element3D.getBoundsInLocal().getHeight()/2);
		return element3D;
	}
	@Override
	public void update(double deltaTime) {
		
	}
}
