package ui.gamescene;

import characteristic.Updateable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ui.floatingpane.FloatingPane;

import java.util.Hashtable;

public class GameScreen extends SubScene implements Updateable {
    private GameScene gameScene;
    private Group root;
    public GameScreen(double w, double h, GameScene gs, Stage window){
        super(new Group(),w,h,false, SceneAntialiasing.DISABLED);
        gameScene = gs;
        root = (Group)getRoot();
        cursorLabelGroup = new Group();
        cursorInfoLabel = new Label("");
        cursorInfoLabel.setTextFill(Color.WHITE);
        cursorInfoLabel.setMouseTransparent(true);
        labelBackGround = new Rectangle(cursorInfoLabel.getBoundsInLocal().getWidth(), cursorInfoLabel.getBoundsInLocal().getHeight());
        labelBackGround.setFill(new Color(0.8, 0.8, 0.8, 0.7));
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
		cursorInfoLabel.setText(text);
		if(text.length() < 1) {
			labelBackGround.setWidth(0);
			labelBackGround.setHeight(0);
		}else {
			labelBackGround.setWidth(cursorLabelGroup.getBoundsInLocal().getWidth());
			labelBackGround.setHeight(cursorLabelGroup.getBoundsInLocal().getHeight());		
		}
	}
	public void setupInfoLabel() {
		this.setOnMouseMoved((e)->{
			cursorLabelGroup.setTranslateX(e.getSceneX()+15+cursorInfoLabel.getBoundsInLocal().getWidth()/2);
			cursorLabelGroup.setTranslateY(e.getSceneY()/*+15+cursorInfoLabel.getBoundsInLocal().getHeight()/2*/);
		});
	}
	
}
