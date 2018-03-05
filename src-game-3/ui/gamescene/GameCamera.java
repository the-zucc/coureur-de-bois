package ui.gamescene;

import java.util.Hashtable;

import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import javafx.scene.PerspectiveCamera;

public class GameCamera extends PerspectiveCamera implements Attachable{
	public GameCamera(){
		
	}

	@Override
	public void update(double secondsPassed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldUpdate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMessageReceived(Hashtable<String, ? extends Object> message) {
		
	}

	@Override
	public void attachTo(AttachableReceiver ar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void detachFromReceiver(AttachableReceiver ar) {
		// TODO Auto-generated method stub
		
	}
}
