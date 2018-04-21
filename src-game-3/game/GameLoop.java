package game;

import javafx.animation.AnimationTimer;
import javafx.scene.PerspectiveCamera;
import ui.gamescene.GameCamera;

public class GameLoop extends AnimationTimer {
	
	long lastFrameNanos;
	private GameCamera gameCamera;
	
	public GameLoop(GameCamera gameCamera) {
		super();
		this.gameCamera = gameCamera;
	}

	@Override
	public void start(){
		lastFrameNanos = System.nanoTime();
		super.start();
	}

	@Override
	public void handle(long arg0) {
		GameLogic.mainLoop((arg0-lastFrameNanos)/1e9);
		gameCamera.update(arg0);
		lastFrameNanos = arg0;
	}

}
