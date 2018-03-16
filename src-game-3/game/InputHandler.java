package game;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;

public class InputHandler {
    private static InputHandler instance;
    public static InputHandler getInstance(){
        if(instance == null)
            instance = new InputHandler();
        return instance;
    }
    private ArrayList<KeyCode> playerBoundKeyCodes;
    private InputHandler(){
        playerBoundKeyCodes = new ArrayList<KeyCode>();
        playerBoundKeyCodes.add(KeyCode.A);
        playerBoundKeyCodes.add(KeyCode.S);
        playerBoundKeyCodes.add(KeyCode.D);
        playerBoundKeyCodes.add(KeyCode.W);
        playerBoundKeyCodes.add(KeyCode.TAB);
        playerBoundKeyCodes.add(KeyCode.Q);
        playerBoundKeyCodes.add(KeyCode.E);
    }
    public void handleKeyDown(KeyEvent e){

    }
}
