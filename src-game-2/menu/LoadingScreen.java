package menu;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class LoadingScreen extends Scene {
	

	public LoadingScreen(Parent arg0, double width, double height, boolean zbuffer, SceneAntialiasing antialiasing) {
		super(arg0, width, height, zbuffer, antialiasing);
		
		Image i = new Image("res/loading.png");
		ImageView iv = new ImageView(i);
		iv.setScaleX(this.getWidth()/i.getWidth());
		iv.setScaleY(this.getHeight()/i.getHeight());
		((AnchorPane)arg0).getChildren().add(iv);
		((AnchorPane)arg0).setRightAnchor(iv, (this.getWidth()-i.getWidth())/2);
		((AnchorPane)arg0).setTopAnchor(iv, (this.getHeight()-i.getHeight())/2);
		//iv.setTranslateX(0);
		
		((AnchorPane)(arg0)).getChildren().add(new Label("loading"));
		//this.setFill(Color.BLACK);
	}

}
