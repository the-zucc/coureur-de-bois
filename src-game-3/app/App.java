package app;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;
import ui.UserInterface;
import ui.menu.Menu;

public class App extends Application {
	
	public static void main(String[] args){
		App.launch(args);
	}
	
	public static double windowWidth = 1280;
	public static double windowHeight = 720;
	
	private static Stage applicationWindow;
	public static Stage getApplicationWindow(){
		return applicationWindow;
	}
	private static UserInterface userInterface;
	public static UserInterface getUserInterface(){
		return userInterface;
	}

	@Override
	public void start(Stage arg0) throws Exception {
		applicationWindow = arg0;
		userInterface = setupUserInterface(windowWidth, windowHeight);
		arg0.setScene(userInterface);
		arg0.show();
	}
	
	private UserInterface setupUserInterface(double width, double height){
		UserInterface ui = new UserInterface(width, height);
		Menu menu = new Menu(windowWidth, windowHeight);
		ui.putSubScene("main_menu", menu);
		ui.setSubScene("main_menu");
		return ui;
	}
}
