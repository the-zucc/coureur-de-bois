package app;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
	//for singleton implementation
	private static GameLoop instance;
	public static GameLoop getInstance() {
		if(instance == null)
			instance = new GameLoop();
		return instance;
	}
	
	//instance variables
	private long lastFrameNanoseconds;
	
	@Override
	public void start() {
		lastFrameNanoseconds = System.nanoTime();
		super.start();
	}
	
	@Override
	public void handle(long arg0) {
		double deltaTime = (arg0 - lastFrameNanoseconds)/1e9;
		Model.getInstance().update(deltaTime);
		GameScene.getInstance().update(deltaTime);
	}

}
