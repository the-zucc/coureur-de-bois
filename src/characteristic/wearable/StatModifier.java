package characteristic.wearable;

import java.util.Hashtable;

public interface StatModifier {
	Hashtable<String, Double> getAttackStats();
	Hashtable<String, Double> getDefenseStats();
}