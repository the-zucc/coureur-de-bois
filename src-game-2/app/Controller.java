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
		//refresh the gameElements list from theMmodel instance
		long nanoTime = System.nanoTime(); 
		model.update(nanoTime);
		//refresh the gameComponents list from the GameScene instance
		gameScene.update(nanoTime);
		//start the game loop
		GameLoop.getInstance().start();
		//start 
		arg0.setScene(GameScene.getInstance());
		//define that the application window is the current stage
		applicationWindow = arg0;
		GameScene.getInstance().setCameraOnPlayer(GameScene.getInstance().getGameCamera(), Model.getInstance().getCurrentPlayer().getId());
		//setCameraToSee(GameScene.getInstance().getGameCamera());
		//show the window.
		arg0.show();
		
	}
//	private static void setCameraToSee(PerspectiveCamera camera) {
//		Animation animation = new Transition() {
//			{
//				setCycleDuration(Duration.millis(500));
//			}
//			protected void interpolate(double frac) {
//				double isometricValueZ = -350;
//				double isometricValueY = -350;
//				double angle = -45; 
//
//				// currentCamera.setTranslateX(isometricValue*frac);
//				camera.setTranslateY(isometricValueY * frac);
//				camera.setTranslateZ(isometricValueZ * frac);
//
//				// camera.setRotationAxis(Rotate.Y_AXIS);
//				// camera.setRotate(80*frac);
//				camera.setRotationAxis(Rotate.X_AXIS);
//				camera.setRotate(angle * frac);
//				// currentCamera.setRotationAxis(Rotate.Z_AXIS);
//				// currentCamera.setRotate(0*frac);
//			}
//		};
//		animation.play();
//	}
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
		GameScene.getInstance().getGameViewPort().setWidth(applicationWindow.getWidth());
		GameScene.getInstance().getGameViewPort().setHeight(applicationWindow.getHeight());
	}
}
