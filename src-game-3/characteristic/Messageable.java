package characteristic;

import java.util.Hashtable;

public interface Messageable {
	public void onMessageReceived(Hashtable<String, ? extends Object> message);
}
