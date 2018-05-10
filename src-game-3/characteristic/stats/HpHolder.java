package characteristic.stats;

import characteristic.MessageReceiver;

public interface HpHolder extends StatHolder, MessageReceiver {
    double getHp();
}
