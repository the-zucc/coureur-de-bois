package ui.menu;

import game.GameLogic;
import game.Map;
import game.settings.Preferences;
import game.settings.Settings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Menu extends SubScene {
	
	private static String BUTTONTEXT_ENABLED = "Enabled";
	private static String BUTTONTEXT_DISABLED = "Disabled";
	private static double TOGGLEBUTTON_WIDTH = 75;
	private static double TOGGLEBUTTON_HEIGHT = 25;
	
	private Group menuRoot;
	
	//the VBox containing the whole menu
	private VBox wholeMenuVBox;
	
	//the VBox containing the Settings
	private VBox settingsVBox;
	private HBox antialiasingHBox;
	private ToggleGroup antialiasingGroup;
	private ToggleButton antialiasingButtonOn;
	private ToggleButton antialiasingButtonOff;
	
	//the VBox containing the game preferences
	private VBox gamePreferencesVBox;
	private HBox mapHeightHBox;
	private TextField mapHeightTextField;
	private HBox mapWidthHBox;
	private TextField mapWidthTextField;
	private HBox waterLevelHBox;
	private TextField waterLevelTextField;
	
	
	//the HBox located at the top of the layout. contains gamePreferencesVBox and settingsVBox
	private HBox topPanelHBox;
	
	//the start game button, located at the bottom of the layout
	private Button startGameButton;
	
	
	public Menu(double w, double h) {
		super(new Group(), w, h, false, SceneAntialiasing.BALANCED);
		
		//VBox for settings
		double spaceSize = 10;
		settingsVBox = buildVBox(spaceSize);
		settingsVBox.setAlignment(Pos.TOP_CENTER);
		Label settings = new Label("settings");
		//settings.setAlignment(Pos.TOP_CENTER);
		settingsVBox.getChildren().add(settings);
		
		
		
		antialiasingHBox = buildHBox(5);
		antialiasingButtonOn = buildToggleButton(true, BUTTONTEXT_ENABLED, TOGGLEBUTTON_WIDTH, TOGGLEBUTTON_HEIGHT);
		antialiasingButtonOff = buildToggleButton(false, BUTTONTEXT_DISABLED, TOGGLEBUTTON_WIDTH, TOGGLEBUTTON_HEIGHT);
		antialiasingGroup = groupToggles(antialiasingButtonOn, antialiasingButtonOff);
		antialiasingHBox.getChildren().addAll(new Label("antialiasing:"),antialiasingButtonOn, antialiasingButtonOff);
		
		
		settingsVBox.getChildren().add(antialiasingHBox);
		
		
		//VBox for game preferences
		spaceSize = 10;
		gamePreferencesVBox = buildVBox(10);
		gamePreferencesVBox.setAlignment(Pos.TOP_CENTER);
		
		spaceSize = 5;
		
		waterLevelHBox = buildHBox(spaceSize);
		waterLevelTextField = new TextField();
		waterLevelHBox.getChildren().addAll(new Label("Water level: "), waterLevelTextField);
		
		mapWidthHBox = buildHBox(spaceSize);
		mapWidthTextField = new TextField();
		mapWidthHBox.getChildren().addAll(new Label("Map width: "), mapWidthTextField);
		
		mapHeightTextField = new TextField();
		mapHeightHBox = buildHBox(spaceSize);
		mapHeightHBox.getChildren().addAll(new Label("Map height: "), mapHeightTextField);
		
		gamePreferencesVBox.getChildren().addAll(mapWidthHBox, mapHeightHBox, waterLevelHBox);
		
		//VBox for containing both top panels
		spaceSize = 15;
		topPanelHBox = buildHBox(spaceSize);
		
		topPanelHBox.getChildren().addAll(gamePreferencesVBox, settingsVBox);
		
		
		//VBox containing the whole menu 
		spaceSize = 20;
		wholeMenuVBox = buildVBox(spaceSize);
		wholeMenuVBox.setAlignment(Pos.TOP_CENTER);
		
		//start game button
		startGameButton = new Button("start game");
		startGameButton.setPrefSize(200,100);
		startGameButton.setOnMouseClicked(e -> {
			action_startGameButton();
		});
		
		wholeMenuVBox.getChildren().add(topPanelHBox);
		wholeMenuVBox.getChildren().add(startGameButton);
		
		menuRoot = (Group)getRoot();
		menuRoot.getChildren().add(wholeMenuVBox);
		wholeMenuVBox.setTranslateX(w/2-wholeMenuVBox.getWidth()/2);
		wholeMenuVBox.setTranslateY(h/2-wholeMenuVBox.getHeight()/2);
	}
	private VBox buildVBox(double spaceSize){
		VBox returnedVBox = new VBox();
		returnedVBox.setPadding(new Insets(spaceSize, spaceSize, spaceSize, spaceSize));
		returnedVBox.setSpacing(spaceSize);
		
		return returnedVBox;
	}
	private HBox buildHBox(double spaceSize){
		HBox returnedHBox = new HBox();
		returnedHBox.setPadding(new Insets(spaceSize, spaceSize, spaceSize, spaceSize));
		returnedHBox.setSpacing(spaceSize);
		
		return returnedHBox;
	}
	private ToggleButton buildToggleButton(boolean enabledByDefault, String buttonText, double width, double height){
		ToggleButton button = new ToggleButton(buttonText);
		button.setPrefSize(width, height);
		button.setSelected(enabledByDefault);
		return button;
	}
	private ToggleGroup groupToggles(ToggleButton... buttons){
		ToggleGroup group = new ToggleGroup();
		for(ToggleButton b:buttons){
			b.setToggleGroup(group);
		}
		return group;
	}
	
	private void action_startGameButton(){
		action_setSettings();
		action_setGamePreferences();
		GameLogic.startGame();
	}
	private void action_setSettings(){
		Settings.setAntialiasing(antialiasingButtonOn.isSelected());
	}
	private void action_setGamePreferences(){
		if(mapWidthTextField.getText().length() >0){
			try{
				double mapWidth = Double.parseDouble(mapWidthTextField.getText());
				Preferences.setMapWidth(mapWidth);
			}catch(Exception e){
				System.out.println("invalid map width.");
			}
		}
		if(mapHeightTextField.getText().length() >0){
			try{
				double mapHeight = Double.parseDouble(mapHeightTextField.getText());
				Preferences.setMapHeight(mapHeight);
			}catch(Exception e){
				System.out.println("invalid map width.");
			}
		}
		if(waterLevelTextField.getText().length() >0){
			try{
				double waterLevel = Double.parseDouble(waterLevelTextField.getText());
				Preferences.setWaterLevel(waterLevel);
			}catch(Exception e){
				System.out.println("invalid map width.");
			}
		}
	}
}
