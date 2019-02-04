package game;

import javafx.animation.AnimationTimer;
import javafx.scene.PerspectiveCamera;
import ui.gamescene.GameCamera;
import ui.gamescene.GameScene;

public class GameLoop extends AnimationTimer {
	
	long lastFrameNanos;
	private GameCamera gameCamera;
	private GameScene gameScene;
	
	public GameLoop(GameCamera gameCamera, GameScene gameScene) {
		super();
		this.gameCamera = gameCamera;
		this.gameScene = gameScene;
	}

	@Override
	public void start(){
		lastFrameNanos = System.nanoTime();
		super.start();
	}

	@Override
	public void handle(long arg0) {
		GameLogic.mainLoop((float)((arg0-lastFrameNanos)/1e9));
		gameCamera.update(arg0);
		gameScene.update(arg0);
		lastFrameNanos = arg0;
	}

}
