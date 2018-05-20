package characteristic;

import java.util.ArrayList;
import java.util.Hashtable;


public interface Messenger{
    void notifyReceivers(String message);
    void notifyReceivers(String message, Object... params);
    void send(String message);
    void send(String message, Object... params);

    ArrayList<MessageReceiver> getReceivers();
    Hashtable<String, ArrayList<MessageReceiver>> getListeners();
    void addReceiver(MessageReceiver o);
    void removeReceiver(MessageReceiver o);
	void setupListener(String message, MessageReceiver receiver);
}
