package characteristic.positionnable;

import characteristic.MessageReceiver;

public interface Reachable extends Positionnable, MessageReceiver{
    float computeReachableRadius();
    float getReachableRadius();
}
