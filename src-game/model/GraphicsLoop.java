package model;

import app.Main;
import javafx.animation.AnimationTimer;

public class GraphicsLoop extends javafx.animation.AnimationTimer {
	private long lastFrameNanos;
	public GraphicsLoop(){
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
		Engine.mainLoop_Graphics(deltaTime);
		Engine.mainLoop(deltaTime);
	}

}
