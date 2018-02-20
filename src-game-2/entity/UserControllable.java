package entity;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public interface UserControllable {
	void onMouseMoved(MouseEvent me);
	void onMouseClicked(MouseEvent me);
	void onKeyDown(KeyEvent ke);
	void onKeyUp(KeyEvent ke);
}
