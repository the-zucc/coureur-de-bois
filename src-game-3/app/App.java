package app;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import ui.UserInterface;
import ui.menu.Menu;

public class App extends Application {
	public static double windowWidth = 1280;
	public static double windowHeight = 720;
	
	private static Stage applicationWindow;
	public static Stage getApplicationWindow(){
		return applicationWindow;
	}

	@Override
	public void start(Stage arg0) throws Exception {
		applicationWindow = arg0;
		
		arg0.setScene(setupUserInterface(windowWidth, windowHeight));
	}
	
	public UserInterface setupUserInterface(double width, double height){
		Menu menu = new Menu(new Group(), windowWidth, windowHeight);
		Group uiRoot = new Group();
		uiRoot.getChildren().add(menu);
		return new UserInterface(uiRoot, width, height);
	}
}
