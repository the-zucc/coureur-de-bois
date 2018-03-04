package ui.gamescene;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.transform.Rotate;

public class GameScene extends SubScene {
	private Group gameRoot;
	private PerspectiveCamera gameCamera;
	public GameScene(double arg1, double arg2) {
		super(new Group(), arg1, arg2, true, SceneAntialiasing.BALANCED);
		gameRoot = (Group)getRoot();
		gameRoot.getChildren().add(new Label("yo"));
		gameCamera = buildGameCamera();
		setCamera(gameCamera);
	}
	public Group getGameRoot(){
		return gameRoot;
	}
	public PerspectiveCamera buildGameCamera(){
		double dist = 1500;
		PerspectiveCamera returnVal = new PerspectiveCamera(true);
		returnVal.setNearClip(10);
		returnVal.setFarClip(20000);
		returnVal.setTranslateY(-dist);
		returnVal.setTranslateZ(-dist);
		returnVal.setRotationAxis(Rotate.X_AXIS);
		returnVal.setRotate(-45);
		return returnVal;
	}
}
