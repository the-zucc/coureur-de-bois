package characteristic.stats;

import java.util.Hashtable;

public interface StatHolder {
    Hashtable<String, Double> computeAttackStats();
    Hashtable<String, Double> computeDefenseStats();
}
