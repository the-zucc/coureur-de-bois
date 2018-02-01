package app;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;

public class MainMenu extends SubScene {
	
	private static MainMenu instance;
	
	public static MainMenu getInstance(){
		if(instance == null)
			instance = new MainMenu(buildMenu(), Controller.getApplicationWindow().getWidth(), Controller.getApplicationWindow().getHeight(), false, SceneAntialiasing.BALANCED);
		return instance;
	}
	
	private MainMenu(Group menu, double width, double height, boolean zbuffer, SceneAntialiasing antialiasing) {
		super(menu, width, height, zbuffer, antialiasing);
	}

	private static Group buildMenu(){
		Group returnVal = new Group();
		
		return returnVal;
	}
}
