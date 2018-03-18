package ui.gamescene;

import game.Map;
import game.settings.Settings;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class GameScene extends SubScene {
	private Group gameRoot;
	private PerspectiveCamera gameCamera;
	public GameScene(double arg1, double arg2) {
		super(new Group(), arg1, arg2, true, Settings.getAntialiasingValue());
		gameRoot = (Group)getRoot();
		gameCamera = new GameCamera(2000, Map.getInstance().getCurrentPlayer());
		setCamera(gameCamera);
	}
	public Group getGameRoot(){
		return gameRoot;
	}
	public void createConnection(Point3D origin, Point3D target, PhongMaterial material) {
		Point3D yAxis = new Point3D(0, 1, 0);
		Point3D diff = target.subtract(origin);
		double height = diff.magnitude();

		Point3D mid = target.midpoint(origin);
		Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

		Point3D axisOfRotation = diff.crossProduct(yAxis);
		double angle = Math.acos(diff.normalize().dotProduct(yAxis));
		Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

		Cylinder line = new Cylinder(1, height);

		line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
		line.setMaterial(material);

		((Group)this.getRoot()).getChildren().addAll(line);
	}
}
