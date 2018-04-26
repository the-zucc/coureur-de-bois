package characteristic;

import java.util.ArrayList;

public interface Messenger{
    void notifyReceivers(String message);
    void notifyReceivers(String message, Object... params);
    void send(String message);
    void send(String message, Object... params);

    ArrayList<MessageReceiver> getReceivers();

    void addReceiver(MessageReceiver o);
    void removeReceiver(MessageReceiver o);
}
