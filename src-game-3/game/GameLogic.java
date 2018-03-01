package game;

import java.util.Hashtable;

import characteristic.Messageable;
import entity.Entity;
import entity.living.LivingEntity;
import javafx.geometry.Point3D;

public class GameLogic {
	private static Point3D gravity = new Point3D(0,10,0);
	public static Point3D getGravity(){
		return gravity;
	}
	
	public static void sendMessage(Messageable m, Hashtable<String, ? extends Object> message){
		m.onMessageReceived(message);
	}
	
	public static Hashtable<String, Object> createSimpleXpMessage(double xp, Messageable sender){
		Hashtable<String, Object> message = new Hashtable<String, Object>();
		message.put("xp", xp);
		message.put("sender", sender);
		return message;
	}
	
	public Hashtable<String, ? extends Object> sendMessageForResponse(Hashtable<String, ? extends Object> message){
		
		return null;
	}
	
	public static void mainLoop(double secondsPassed){
		
		Map.getInstance().update(secondsPassed);
	}

	public static void startGame() {
		
	}
}
