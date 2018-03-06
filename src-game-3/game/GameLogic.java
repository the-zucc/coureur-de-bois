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

public class GameLogic {
	private static double meterLength = 50;
	
	private static long updateCount = 0;
	
	private static GameLoop loop;
	
	public static GameLoop getGameLoop(){
		return loop;
	}
	
	private static Point3D gravity = new Point3D(0,9.81*2*meterLength,0);
	
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
		GameScene scene = new GameScene(App.windowWidth, App.windowHeight);
		App.getUserInterface().putSubScene("game", scene);
		App.getUserInterface().setSubScene("game");
		((Group)scene.getRoot()).getChildren().add(map.getComponent());
		loop = new GameLoop();
		loop.start();
	}

	public static double getMeterLength() {
		return meterLength;
	}
}
