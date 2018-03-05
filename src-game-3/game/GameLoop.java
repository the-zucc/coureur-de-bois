package game;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
	
	long lastFrameNanos;
	@Override
	public void start(){
		lastFrameNanos = System.nanoTime();
		super.start();
	}

	@Override
	public void handle(long arg0) {
		GameLogic.mainLoop((arg0-lastFrameNanos)/1e9);
		lastFrameNanos = arg0;
	}

}
