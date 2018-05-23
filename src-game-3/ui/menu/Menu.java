package ui.menu;

import game.GameLogic;
import game.settings.Preferences;
import game.settings.Settings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
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
	private HBox treeCountHBox;
	private TextField treeCountTextField;
	private HBox villageCountHBox;
	private TextField villageCountTextField;
	private HBox villageRadiusHBox;
	private TextField villageRadiusTextField;
	private HBox villageVillagerCountHBox;
	private TextField villageVillagerCountTextField;
	private HBox villageTipiCountHBox;
	private TextField villageTipiCountTextField;

	
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
		waterLevelTextField.setText(String.valueOf(Preferences.getWaterLevel()));
		waterLevelHBox.getChildren().addAll(new Label("Water level: "), waterLevelTextField);
		
		mapWidthHBox = buildHBox(spaceSize);
		mapWidthTextField = new TextField();
		mapWidthTextField.setText(String.valueOf(Preferences.getMapWidth()));
		mapWidthHBox.getChildren().addAll(new Label("Map width: "), mapWidthTextField);
		
		mapHeightHBox = buildHBox(spaceSize);
		mapHeightTextField = new TextField();
		mapHeightTextField.setText(String.valueOf(Preferences.getMapHeight()));
		mapHeightHBox.getChildren().addAll(new Label("Map height: "), mapHeightTextField);
		
		treeCountHBox = buildHBox(spaceSize);
		treeCountTextField = new TextField();
		treeCountTextField.setText(String.valueOf(Preferences.getTreeCount()));
		treeCountHBox.getChildren().addAll(new Label("Tree count: "), treeCountTextField);

		villageRadiusHBox = buildHBox(spaceSize);
		villageRadiusTextField = new TextField();
		villageRadiusTextField.setText(String.valueOf(Preferences.getVillageRadius()));
		villageRadiusHBox.getChildren().addAll(new Label("Village size: "), villageRadiusTextField);

		villageCountHBox = buildHBox(spaceSize);
		villageCountTextField = new TextField();
		villageCountTextField.setText(String.valueOf(Preferences.getVillageCount()));
		villageCountHBox.getChildren().addAll(new Label("Village count: "), villageCountTextField);

		villageVillagerCountHBox = buildHBox(spaceSize);
		villageVillagerCountTextField = new TextField();
		villageVillagerCountTextField.setText(String.valueOf(Preferences.getVillageVillagerCount()));
		villageVillagerCountHBox.getChildren().addAll(new Label("Villagers/village:"), villageVillagerCountTextField);

		villageTipiCountHBox = buildHBox(spaceSize);
		villageTipiCountTextField = new TextField();
		villageTipiCountTextField.setText(String.valueOf(Preferences.getVillageTipiCount()));
		villageTipiCountHBox.getChildren().addAll(new Label("Tipis/Village:"), villageTipiCountTextField);

		gamePreferencesVBox.getChildren().addAll(mapWidthHBox, mapHeightHBox, treeCountHBox, waterLevelHBox, villageCountHBox, villageRadiusHBox, villageTipiCountHBox, villageVillagerCountHBox);
		
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
		startGameButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				action_startGameButton();
			}
		});
		
		wholeMenuVBox.getChildren().add(topPanelHBox);
		wholeMenuVBox.getChildren().add(startGameButton);
		
		menuRoot = (Group)getRoot();
		menuRoot.getChildren().add(wholeMenuVBox);
		//wholeMenuVBox.setTranslateX(w/2-wholeMenuVBox.getWidth()/2);
		//wholeMenuVBox.setTranslateY(h/2-wholeMenuVBox.getHeight()/2);

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
		if(mapWidthTextField.getText().length() > 0){
			try{
				double mapWidth = Double.parseDouble(mapWidthTextField.getText());
				System.out.println("mapwidth read: "+mapWidth);
				Preferences.setMapWidth(mapWidth);
			}catch(Exception e){
				System.out.println("invalid map width.");
			}
		}
		if(mapHeightTextField.getText().length() > 0){
			try{
				double mapHeight = Double.parseDouble(mapHeightTextField.getText());
				System.out.println("mapHeight read: "+mapHeight);
				Preferences.setMapHeight(mapHeight);
			}catch(Exception e){
				System.out.println("invalid map width.");
			}
		}
		if(waterLevelTextField.getText().length() > 0){
			try{
				float waterLevel = -(Float.parseFloat(waterLevelTextField.getText()));
				Preferences.setWaterLevel(waterLevel);
			}catch(Exception e){
				System.out.println("invalid map width.");
			}
		}
		if(treeCountTextField.getText().length() > 0){
			try{
				int treeCount = Integer.parseInt(treeCountTextField.getText());
				Preferences.setTreeCount(treeCount);
			}catch(Exception e){
				System.out.println("invalid tree count.");
			}
		}
		if(villageRadiusTextField.getText().length() > 0){
			try{
				double villageRadius = Double.parseDouble(villageRadiusTextField.getText());
				Preferences.setVillageRadius(villageRadius);
			}catch(Exception e){
				System.out.println("invalid village radius.");
			}
		}
		if(villageCountTextField.getText().length() > 0){
			try{
				int villageCount = Integer.parseInt(villageCountTextField.getText());
				Preferences.setVillageCount(villageCount);
			}catch(Exception e){
				System.out.println("invalid village count.");
			}
		}
		if(villageTipiCountTextField.getText().length() > 0){
			try{
				int tipiCount = Integer.parseInt(villageTipiCountTextField.getText());
				Preferences.setVillageTipiCount(tipiCount);
			}catch(Exception e){
				System.out.println("invalid tipi count.");
			}

		}
		if(villageVillagerCountTextField.getText().length() > 0){
			try{
				int villagerCount = Integer.parseInt(villageTipiCountTextField.getText());
				Preferences.setVillageVillagerCount(villagerCount);
			}catch(Exception e){
				System.out.println("invalid tipi count.");
			}
		}
	}
}
