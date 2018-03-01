package ui.menu;

import game.GameLogic;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;

public class Menu extends SubScene {
	private Group menuRoot;
	private Button startGameButton;

	public Menu(Parent arg0, double w, double h) {
		super(arg0, w, h, false, SceneAntialiasing.BALANCED);
		menuRoot = (Group)arg0;
		startGameButton = new Button("start game");
		
		menuRoot.getChildren().add(startGameButton);
	}
	
	private void action_startGameButton(){
		GameLogic.startGame();
	}
}
