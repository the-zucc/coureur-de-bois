package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class Controller extends Application{
		
	public static double debug_gameCameraDistance = 800;
	
	private static Stage applicationWindow;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		GameScene gameScene = GameScene.getInstance();
		GameLoop.getInstance().start();
		
		arg0.setScene(gameScene);
		applicationWindow = arg0;
		
		arg0.show();
	}
	public static Stage getApplicationWindow() {
		return applicationWindow;
	}
}
