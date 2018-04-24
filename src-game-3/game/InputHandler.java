package game;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

import app.App;

public class InputHandler {
	private Map map;
    private ArrayList<KeyCode> playerBoundKeyCodes;
    public InputHandler(Map map){
        playerBoundKeyCodes = new ArrayList<KeyCode>();
        playerBoundKeyCodes.add(KeyCode.A);
        playerBoundKeyCodes.add(KeyCode.S);
        playerBoundKeyCodes.add(KeyCode.D);
        playerBoundKeyCodes.add(KeyCode.W);
        playerBoundKeyCodes.add(KeyCode.TAB);
        playerBoundKeyCodes.add(KeyCode.Q);
        playerBoundKeyCodes.add(KeyCode.E);
        playerBoundKeyCodes.add(KeyCode.SPACE);
        playerBoundKeyCodes.add(KeyCode.SHIFT);
        this.map = map;
    }
    public void handleKeyDown(KeyEvent e){
    	if(playerBoundKeyCodes.contains(e.getCode())){
    		map.getCurrentPlayer().onKeyPressed(e);
    	}
    	else{
    		if(e.getCode().equals(KeyCode.F11)){
    			App.getApplicationWindow().setFullScreen(!App.getApplicationWindow().isFullScreen());
    		}
    	}
    }
    public void handleKeyUp(KeyEvent e){
    	if(playerBoundKeyCodes.contains(e.getCode())){
    		map.getCurrentPlayer().onKeyReleased(e);
    	}
    }
}
