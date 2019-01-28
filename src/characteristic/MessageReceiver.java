package characteristic;

import util.MessageCallback;

import java.util.ArrayList;
import java.util.Hashtable;

public interface MessageReceiver {
	ArrayList<Messenger> getMessengers();
	ArrayList<String> getReceivedMessages();
	ArrayList<Object[]> getReceivedParams();
    void processCallbackQueue();

    void receiveMessage(String message, Object... params);
    void receiveMessage(String message);

    void accept(String message, MessageCallback callback);
    Hashtable<String, MessageCallback> getAccepts();
    void listenTo(Messenger messenger);
}
