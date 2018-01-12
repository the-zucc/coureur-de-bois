package model;

import javafx.animation.AnimationTimer;

public class ControlerLoop extends javafx.animation.AnimationTimer {
	private long lastFrameNanos;
	public ControlerLoop(){
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
		Engine.MainLoop(deltaTime);
	}

}
