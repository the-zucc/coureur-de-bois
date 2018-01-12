package entity;

import java.util.concurrent.ThreadLocalRandom;

import app.Main;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class Tree extends GameElement{
	public Tree(Point3D position) {
		super(position);
		element3D = buildElement3D();
		parent = (Group)Main.getInstance().getSubScene("principal").getRoot();
		parent.getChildren().addAll(element3D);
		updateElement3DPosition();
	}
	public static Group buildElement3D() {
		Group element3D = new Group();
		Box trunk = new Box(30, 100, 30);
		Box leaves = new Box(100, 80, 100);
		leaves.setTranslateY(-(trunk.getHeight()+leaves.getHeight()/2));
		leaves.setMaterial(new PhongMaterial(Color.GREEN));
		trunk.setMaterial(new PhongMaterial(Color.SADDLEBROWN));
		trunk.setTranslateY(-trunk.getHeight()/2);
		element3D.getChildren().addAll(trunk, leaves);
		double value = ThreadLocalRandom.current().nextDouble(1.5)+1;
		element3D.setScaleX(value);
		element3D.setScaleY(value);
		element3D.setScaleZ(value);
		return element3D;
	}
	@Override
	public void update(double deltaTime) {
		
	}
}
