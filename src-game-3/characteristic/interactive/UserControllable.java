package characteristic.interactive;

import javafx.scene.input.KeyEvent;

public interface UserControllable {
	public void onKeyPressed(KeyEvent ke);
	public void onKeyReleased(KeyEvent ke);
}
