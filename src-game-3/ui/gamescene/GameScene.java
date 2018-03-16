package ui.gamescene;

import game.Map;
import game.settings.Settings;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;

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
	
}
