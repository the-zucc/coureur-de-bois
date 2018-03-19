package ui.gamescene;

import characteristic.Messageable;
import characteristic.Updateable;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import ui.floatingpane.FloatingPane;

import java.util.Hashtable;

public class GameScreen extends SubScene implements Updateable, Messageable{
    private GameScene gameScene;
    private Group root;
    private Hashtable<String, FloatingPane> floatingPanes;
    public GameScreen(double w, double h, GameScene gs){
        super(new Group(),w,h,false, SceneAntialiasing.DISABLED);
        gameScene = gs;
        root = (Group)getRoot();
        root.getChildren().add(gs);
        floatingPanes = new Hashtable<String, FloatingPane>();
    }
    public GameScene getGameScene(){
        return gameScene;
    }

    @Override
    public void onMessageReceived(Hashtable<String, ?> message) {
        if(message.containsKey("show_info_pane")){
            Hashtable<String, Hashtable<String, ?>> messageData = (Hashtable<String, Hashtable<String, ?>>)message;
            if(messageData.containsKey("entity")){

            }
            if(messageData.containsKey("pane")){

            }
            if(messageData.containsKey("component_to_follow")){

            }
        }
    }

    @Override
    public void update(double secondsPassed) {

    }

    @Override
    public boolean shouldUpdate() {
        return false;
    }
    
    public FloatingPane getFloatingPane(String key){
    	return floatingPanes.get(key);
    }

}
