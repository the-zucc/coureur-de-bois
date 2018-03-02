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

	public Menu(double w, double h) {
		super(new Group(), w, h, false, SceneAntialiasing.BALANCED);
		menuRoot = (Group)getRoot();
		startGameButton = new Button("start game");
		startGameButton.setOnMouseClicked(e -> {
			action_startGameButton();
		});
		menuRoot.getChildren().add(startGameButton);
	}
	
	private void action_startGameButton(){
		GameLogic.startGame();
	}
}
