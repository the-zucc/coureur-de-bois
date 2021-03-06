package game;

import app.App;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import ui.gamescene.GameScene;
import ui.gamescene.GameScreen;

public class GameLogic {

	private static float meterLength = 5;

	private static long updateCount = 0;
	
	private static GameLoop loop;
	
	public static GameLoop getGameLoop(){
		return loop;
	}
	
	private static Point3D gravity = new Point3D(0,9.81*10*meterLength,0);
	
	public static Point3D getGravity() {
		return gravity;
	}
	
	public static void mainLoop(float secondsPassed) {
		Map currentMap = Map.getMainMap().getCurrentMap();
		if(currentMap != null){
			currentMap.update(secondsPassed);
		}
		else{
			Map.getMainMap().update(secondsPassed);
		}
		updateCount++;
	}

	public static void startGame() {
		Map map = Map.getMainMap();
		GameScene scene = new GameScene(App.windowWidth, App.windowHeight, App.getApplicationWindow(), map, map.getMessenger());
		GameScreen screen = new GameScreen(App.windowWidth, App.windowHeight, scene, App.getApplicationWindow());
		
		App.getUserInterface().putSubScene("game", screen);
		App.getUserInterface().setSubScene("game");
		App.getUserInterface().setMapAndBindControls(map);
		((Group)scene.getRoot()).getChildren().add(map.getComponent());
		
		loop = new GameLoop(scene.getGameCamera(), scene);
		loop.start();
	}

	public static float getMeterLength() {
		return meterLength;
	}


}
