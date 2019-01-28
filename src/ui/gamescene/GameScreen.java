package ui.gamescene;

import characteristic.Updateable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ui.floatingpane.FloatingPane;
import util.NodeUtils;

import java.io.IOException;
import java.util.Hashtable;

public class GameScreen extends SubScene implements Updateable{
    private GameScene gameScene;
    private Group root;
    public Group getUiRoot() {
    	return root;
    }
    private ProgressBar manaBar;
    public void showNewMana(double mana, double maxMana) {
    	manaBar.setProgress(mana/maxMana);
    }
    private ProgressBar hpBar;
    public void showNewHP(double hp, double maxHp) {
    	hpBar.setProgress(hp/maxHp);
    }
    public GameScreen(double w, double h, GameScene gs, Stage window){
        super(new Group(),w,h,false, SceneAntialiasing.DISABLED);
        try {
			setRoot(FXMLLoader.load(getClass().getResource("/fxml/player_stats_ingame.fxml")));
			root = (Group)(NodeUtils.getChildByID(getRoot(), "uiRoot"));
			gameScene = (GameScene)(NodeUtils.getChildByID(getRoot(), "gameScene"));
			manaBar = (ProgressBar)(NodeUtils.getChildByID(getRoot(), "manaBar"));
			hpBar = (ProgressBar)(NodeUtils.getChildByID(getRoot(), "hpBar"));
		} catch (IOException e) {
			root = (Group)getRoot();
			gameScene = gs;
			e.printStackTrace();
		}
        cursorLabelGroup = new Group();
        cursorInfoLabel = new Label("");
        cursorInfoLabel.setTextFill(Color.WHITE);
        cursorInfoLabel.setMouseTransparent(true);
        labelBackGround = new Rectangle(cursorInfoLabel.getBoundsInLocal().getWidth(), cursorInfoLabel.getBoundsInLocal().getHeight());
        labelBackGround.setFill(new Color(0.2, 0.2, 0.2, 0.7));
        labelBackGround.setMouseTransparent(true);
        cursorLabelGroup.getChildren().add(labelBackGround);
        cursorLabelGroup.getChildren().add(cursorInfoLabel);
        setupInfoLabel();
        root.getChildren().add(gs);
        root.getChildren().add(cursorLabelGroup);
        window.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GameScreen.this.setWidth(newValue.doubleValue());
            }
        });
        window.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                GameScreen.this.setHeight(newValue.doubleValue());
            }
        });
    }
    public GameScene getGameScene(){
        return gameScene;
    }

    @Override
    public void update(double secondsPassed) {

    }

    @Override
    public boolean shouldUpdate() {
        return false;
    }
    
    /**
     * pour le label qui suit la souris
     */
    private Group cursorLabelGroup;
    private Label cursorInfoLabel;
    private Rectangle labelBackGround;
	public Label getCursorInfoLabel() {
		return cursorInfoLabel;
	}
	public void setMouseTooltipText(String text) {
		if(text != null){
			cursorInfoLabel.setText(text);
			if(text.length() < 1) {
				labelBackGround.setWidth(0);
				labelBackGround.setHeight(0);
			}else {
				labelBackGround.setWidth(cursorInfoLabel.getBoundsInLocal().getWidth());
				labelBackGround.setHeight(cursorInfoLabel.getBoundsInLocal().getHeight());
			}
		}
	}
	public void setupInfoLabel() {
		double hSpacing = 4;
		double vSpacing = 2;
		cursorInfoLabel.setTranslateX(hSpacing);
		cursorInfoLabel.setTranslateY(vSpacing);
		this.setOnMouseMoved((e)->{
			cursorLabelGroup.setTranslateX(e.getSceneX()+15+cursorInfoLabel.getBoundsInLocal().getWidth()/2);
			cursorLabelGroup.setTranslateY(e.getSceneY()/*+15+cursorInfoLabel.getBoundsInLocal().getHeight()/2*/);
			if(cursorInfoLabel.getText().length() > 0) {
				labelBackGround.setWidth(cursorInfoLabel.getBoundsInLocal().getWidth()+hSpacing*2);
				labelBackGround.setHeight(cursorInfoLabel.getBoundsInLocal().getHeight()+vSpacing*2);				
			}else {
				labelBackGround.setWidth(0);
				labelBackGround.setHeight(0);
			}
		});
		cursorLabelGroup.setScaleX(2);
		cursorLabelGroup.setScaleY(2);
		cursorLabelGroup.setScaleZ(2);
	}
	
}
