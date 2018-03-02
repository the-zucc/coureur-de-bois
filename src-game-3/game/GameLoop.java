package game;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {

	@Override
	public void handle(long arg0) {
		GameLogic.mainLoop(0);
	}

}
