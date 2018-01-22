package app;

import java.awt.Dimension;
import java.awt.Toolkit;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Controller extends Application{
		
	public static double debug_gameCameraDistance = 1000;
	
	private static Stage applicationWindow;
	
	public static Stage getApplicationWindow() {
		return applicationWindow;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage arg0) throws Exception {
		//instanciate the model
		Model model = Model.getInstance();
		//instanciate the game scene
		GameScene gameScene = GameScene.getInstance();
		
		//instanciate the UI
		UI ui = UI.getInstance();
		
		//refresh the gameElements list from theMmodel instance
		long nanoTime = System.nanoTime(); 
		model.update(nanoTime);
		//refresh the gameComponents list from the GameScene instance
		gameScene.update(nanoTime);
		//start the game loop
		GameLoop.getInstance().start();
		//start 
		arg0.setScene(ui);
		//define that the application window is the current stage
		applicationWindow = arg0;
		GameScene.getInstance().setCameraOnPlayer(GameScene.getInstance().getGameCamera(), Model.getInstance().getCurrentPlayer().getId());
		
		//show the window.
		arg0.show();
		
	}
	
	/**
	 * exits the fullscreen mode of the applicationWindow. it also centers the window on screen, as well as scales the subscene accordingly.
	 */
	public static void exitFullScreen() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		applicationWindow.setFullScreen(false);
		applicationWindow.setHeight(height/2);
		applicationWindow.setWidth(width/2);
		applicationWindow.setX((width-applicationWindow.getWidth())/2);
		applicationWindow.setY((height-applicationWindow.getHeight())/2);
		updateScreenResolution();
	}
	/**
	 * scales the subsene with the stage
	 */
	public static void updateScreenResolution() {
		GameScene.getInstance().setWidth(applicationWindow.getWidth());
		GameScene.getInstance().setHeight(applicationWindow.getHeight());
	}
}
