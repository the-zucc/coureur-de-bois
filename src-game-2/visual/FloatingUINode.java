package visual;

import app.Controller;
import app.GameScene;
import app.UI;
import entity.Entity;
import entity.Mob;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import util.Updateable;

/**
 * Class for an information pane. this UI element can be linked to any {@link Entity} to show its stats to the user. 
 * @author Laurier
 */
public class FloatingUINode extends Group implements Updateable{
	
	private static double margin = 5;
	private Entity linkedEntity;
	private GameComponent linkedComponent;
	private static double textScale = 1.5;
	private Label hpLabel;
	
	public FloatingUINode(Entity linkedEntity) {
		this.linkedEntity = linkedEntity;
		this.setId(linkedEntity.getId()+"_info");
		this.getChildren().add(new Rectangle(0, 0, 100, 100));
		hpLabel = new Label("HP:");
		hpLabel.setTextFill(Color.AQUA);
		hpLabel.setScaleX(textScale);
		hpLabel.setScaleY(textScale);
		hpLabel.setScaleZ(textScale);
		hpLabel.setTranslateX(15);
		this.getChildren().add(hpLabel);
		this.linkedComponent = GameScene.getInstance().getComponent(linkedEntity.getId());
		this.setMouseTransparent(true);
		this.update(0);
	}

	@Override
	public void update(double deltaTime) {
		
		Bounds linkedEntityBounds = linkedComponent.localToScreen(linkedComponent.getBoundsInLocal());
		//System.out.println(linkedEntityBounds);
		
		double x = linkedEntityBounds.getMaxX() - Controller.getApplicationWindow().getX() + margin;
		double y = linkedEntityBounds.getMinY() - Controller.getApplicationWindow().getY() - this.getBoundsInLocal().getHeight()/2;
		
		hpLabel.setText("HP:"+((Mob)linkedEntity).getHp());
		
		this.setTranslateX(x);
		this.setTranslateY(y);
	}
}
