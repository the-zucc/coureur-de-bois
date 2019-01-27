package characteristic.positionnable;

import characteristic.MessageReceiver;

public interface Reachable extends Positionnable{
    double computeReachableRadius();
    double getReachableRadius();
}
