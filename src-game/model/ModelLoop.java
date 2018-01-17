package model;

import javafx.animation.AnimationTimer;

public class ModelLoop extends AnimationTimer{
	
	private long lastFrameNanos;
	
	public ModelLoop(){
		super();
	}
	public void start() {
		lastFrameNanos = System.nanoTime();
		super.start();
	}
	@Override
	public void handle(long now) {
		double deltaTime = (now-lastFrameNanos)/1e9;
		lastFrameNanos = now;
		Engine.mainLoop(deltaTime);
		
	}
}
