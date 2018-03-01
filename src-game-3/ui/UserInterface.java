package ui;

import characteristic.Updateable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;

public class UserInterface extends Scene implements Updateable {

	public UserInterface(Parent root, double width, double height) {
		super(root, width, height, false, SceneAntialiasing.DISABLED);
	}

	@Override
	public void update(double secondsPassed) {
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}

}
