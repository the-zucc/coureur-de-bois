package ui.gamescene;

import characteristic.Updateable;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
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
        cursorInfoLabel = new Label("label1");
        cursorInfoLabel.setTextFill(Color.WHITE);
        cursorInfoLabel.setMouseTransparent(true);
        setupInfoLabel();
        root.getChildren().add(gs);
        root.getChildren().add(cursorInfoLabel);
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
    private Label cursorInfoLabel;
	public Label getCursorInfoLabel() {
		return cursorInfoLabel;
	}
	public void setMouseTooltipText(String text) {
		cursorInfoLabel.setText(text);
	}
	public void setupInfoLabel() {
		this.setOnMouseMoved((e)->{
			cursorInfoLabel.setTranslateX(e.getSceneX()+15+cursorInfoLabel.getWidth()/2);
			cursorInfoLabel.setTranslateY(e.getSceneY()+15+cursorInfoLabel.getHeight()/2);
		});
		cursorInfoLabel.setScaleX(2);
		cursorInfoLabel.setScaleY(2);
		cursorInfoLabel.setScaleZ(2);
	}
	
}
