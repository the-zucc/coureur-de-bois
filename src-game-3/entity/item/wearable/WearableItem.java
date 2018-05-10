package entity.item.wearable;

import characteristic.wearable.Wearable;

public abstract class WearableItem {
    public WearableItem(Wearable wearable){
        this.wearable = wearable;
    }
    private Wearable wearable;
    public Wearable getWearable(){
        return wearable;
    }
}
