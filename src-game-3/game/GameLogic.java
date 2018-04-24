package game;

import java.util.Hashtable;

import app.App;
import characteristic.ComponentOwner;
import characteristic.Messageable;
import entity.Entity;
import entity.living.LivingEntity;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import ui.gamescene.GameScene;
import ui.gamescene.GameScreen;

public class GameLogic {
	private static double meterLength = 5;
	
	private static long updateCount = 0;
	
	private static GameLoop loop;
	
	public static GameLoop getGameLoop(){
		return loop;
	}
	
	private static Point3D gravity = new Point3D(0,9.81*10*meterLength,0);
	
	public static Point3D getGravity() {
		return gravity;
	}
	
	public static void sendMessage(Messageable m, Hashtable<String, ? extends Object> message) {
		m.onMessageReceived(message);
	}
	
	public static Hashtable<String, Object> createSimpleXpMessage(double xp, Messageable sender) {
		Hashtable<String, Object> message = new Hashtable<String, Object>();
		message.put("xp", xp);
		message.put("sender", sender);
		return message;
	}
	
	public Hashtable<String, ? extends Object> sendMessageForResponse(Hashtable<String, ? extends Object> message) {
		return null;
	}
	
	public static void mainLoop(double secondsPassed) {
		Map.getInstance().update(secondsPassed);
		updateCount++;
	}

	public static void startGame() {
		Map map = Map.getInstance();
		GameScene scene = new GameScene(App.windowWidth, App.windowHeight, App.getApplicationWindow(), map);
		GameScreen screen = new GameScreen(App.windowWidth, App.windowHeight, scene, App.getApplicationWindow());
		App.getUserInterface().putSubScene("game", screen);
		App.getUserInterface().setSubScene("game");
		App.getUserInterface().setMapAndBindControls(map);
		((Group)scene.getRoot()).getChildren().add(map.getComponent());
		loop = new GameLoop(scene.getGameCamera());
		loop.start();
	}

	public static double getMeterLength() {
		return meterLength;
	}
}
