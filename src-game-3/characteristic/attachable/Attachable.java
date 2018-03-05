package characteristic.attachable;

import characteristic.Updateable;
import characteristic.Messageable;

public interface Attachable extends Updateable, Messageable{
	public void attachTo(AttachableReceiver ar);
	public void detachFromReceiver(AttachableReceiver ar);
}
