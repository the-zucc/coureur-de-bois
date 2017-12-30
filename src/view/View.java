package view;
import java.util.ArrayList;
import java.util.Hashtable;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View extends Application{
	private static View instance;
	private Hashtable<String, Group> roots;
	private Hashtable<String, Scene> scenes;
	private Scene currentScene;
	private Group currentRoot;
	private Stage stage;
	
	public View getInstance(){
		if(instance == null){
			instance = new View();
		}
		return instance;
	}
	
	@Override
	public void start(Stage arg0) throws Exception {
		this.stage = arg0;//pour rendre la fenêtre accessible de n'import où
	}
	public Scene getCurrentScene(){
		return currentScene;
	}
	public Group getCurrentRoot(){
		return currentRoot;
	}
	public void setMode(String nomMode){
		currentScene = scenes.get(nomMode);
		currentRoot = roots.get(nomMode);
		stage.setScene(currentScene);
		
	}
	
}
