package characteristic.wearable;

import characteristic.MessageReceiver;
import characteristic.attachable.Attachable;
import entity.item.wearable.WearableItem;

public interface Wearable extends Attachable, MessageReceiver {
    WearableItem getItem();
    WearableItem buildItem();
    void onWearableWorn();
}
