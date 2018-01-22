package visual;

import app.Controller;
import app.GameScene;
import app.UI;
import entity.GameElement;
import entity.Mob;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import util.Updateable;

/**
 * Class for an information pane. this UI element can be linked to any {@link GameElement} to show its stats to the user. 
 * @author Laurier
 */
public class InformationPane extends Group implements Updateable{
	
	private static double margin = 5;
	private GameElement linkedElement;
	private GameComponent linkedComponent;
	private static double textScale = 1.5;
	private Label hpLabel;
	
	public InformationPane(GameElement linkedElement) {
		this.linkedElement = linkedElement;
		this.setId(linkedElement.getId()+"_info");
		this.getChildren().add(new Rectangle(0, 0, 100, 100));
		hpLabel = new Label("HP:");
		hpLabel.setTextFill(Color.AQUA);
		hpLabel.setScaleX(textScale);
		hpLabel.setScaleY(textScale);
		hpLabel.setScaleZ(textScale);
		hpLabel.setTranslateX(15);
		this.getChildren().add(hpLabel);
		this.linkedComponent = GameScene.getInstance().getComponent(linkedElement.getId());
		this.setMouseTransparent(true);
		this.update(0);
	}

	@Override
	public void update(double deltaTime) {
		
		Bounds linkedElementBounds = linkedComponent.localToScreen(linkedComponent.getBoundsInLocal());
		//System.out.println(linkedElementBounds);
		
		double x = linkedElementBounds.getMaxX() - Controller.getApplicationWindow().getX() + margin;
		double y = linkedElementBounds.getMinY() - Controller.getApplicationWindow().getY() - this.getBoundsInLocal().getHeight()/2;
		
		hpLabel.setText("HP:"+((Mob)linkedElement).getHp());
		
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
}
