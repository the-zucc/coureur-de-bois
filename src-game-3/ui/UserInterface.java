package ui;

import java.util.Hashtable;

import characteristic.Updateable;
import game.InputHandler;
import game.Map;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import ui.gamescene.GameScene;
import ui.gamescene.GameScreen;
import ui.menu.Menu;

public class UserInterface extends Scene implements Updateable {
	
	private Hashtable<String, SubScene> subScenes;
	private SubScene currentSubScene;
	private GameScene gameScene;
	private GameScreen gameScreen;
	private Group uiRoot;
	private Map map;
	private InputHandler inputHandler;
	

	public UserInterface(double width, double height) {
		super(new Group(), width, height, false, SceneAntialiasing.DISABLED);
		uiRoot = (Group)getRoot();
		subScenes = new Hashtable<String, SubScene>();
		Menu menu = new Menu(width, height);
		putSubScene("main_menu", menu);
		setSubScene("main_menu");
	}

	@Override
	public void update(double secondsPassed) {
		
	}
	public void setMapAndBindControls(Map map){
		this.map = map;
		inputHandler = new InputHandler(map);
		bindGameControls();
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}
	public SubScene getCurrentSubScene(){
		return currentSubScene;
	}
	public void setSubScene(String key){
		if(subScenes.containsKey(key)){
			uiRoot.getChildren().remove(currentSubScene);
			
			currentSubScene = subScenes.get(key);
			uiRoot.getChildren().add(currentSubScene);
		}
	}
	public void putSubScene(String key, SubScene subScene){
		subScenes.put(key, subScene);
		if(key.equals("game")) {
			gameScene = ((GameScreen)subScene).getGameScene();
			gameScreen = (GameScreen)subScene;
		}
	}

	public GameScene getGameScene() {
		return gameScene;
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void bindGameControls(){
		this.setOnKeyPressed(new EventHandler<KeyEvent>(){
			
			@Override
			public void handle(KeyEvent event) {
				try{
					inputHandler.handleKeyDown(event);
				}catch(Exception ex){
					
				}
			}
		});
		this.setOnKeyReleased(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent event){
				try{
					inputHandler.handleKeyUp(event);
				}catch(Exception ex){
					
				}
			}
		});
	}
}
