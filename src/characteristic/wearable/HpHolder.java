package characteristic.wearable;

import characteristic.MessageReceiver;

public interface HpHolder extends MessageReceiver{
	double getHp();
	void takeDamage(double amount, MessageReceiver attacker);
}
