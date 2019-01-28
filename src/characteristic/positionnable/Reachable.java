package characteristic.positionnable;

import characteristic.MessageReceiver;

public interface Reachable extends Positionnable, MessageReceiver{
    double computeReachableRadius();
    double getReachableRadius();
}
