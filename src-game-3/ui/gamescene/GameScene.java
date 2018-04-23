package ui.gamescene;

import game.GameLogic;
import game.Map;
import game.settings.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Stage;
import visual.Utils3D;

public class GameScene extends SubScene {
	private Group gameRoot;
	private GameCamera gameCamera;
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	public GameScene(double arg1, double arg2, Stage window, Map map) {
		super(new Group(), arg1, arg2, true, Settings.getAntialiasingValue());
		gameRoot = (Group)getRoot();
		gameCamera = new GameCamera(20*GameLogic.getMeterLength(), Map.getInstance().getCurrentPlayer(), map);
		setCamera(gameCamera);
		//Utils3D.lookat(gameCamera, map.getCurrentPlayer().getPosition());
		window.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				GameScene.this.setWidth(newValue.doubleValue());
			}
		});
		window.heightProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				GameScene.this.setHeight(newValue.doubleValue());
			}
		});
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

		Cylinder line = new Cylinder(5, height);

		line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
		line.setMaterial(material);

		((Group)this.getRoot()).getChildren().addAll(line);
	}
}
