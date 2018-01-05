package model;

import javafx.animation.AnimationTimer;

public class ControlerLoop extends javafx.animation.AnimationTimer {

	@Override
	public void handle(long now) {
		Engine.MainLoop();
	}

}
