package characteristic.wearable;

import java.util.ArrayList;
import java.util.Hashtable;

public interface StatHolder extends HpHolder{
	Hashtable<String, Double> getAttackStats();
	Hashtable<String, Double> getDefenseStats();
	ArrayList<StatModifier> getActiveModifiers();
	void disableModifier(StatModifier sm);
}
