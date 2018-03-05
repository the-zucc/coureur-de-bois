package characteristic.attachable;

import java.util.ArrayList;

import characteristic.Messageable;
import characteristic.Updateable;

public interface AttachableReceiver extends Updateable, Messageable{
	public void attach(Attachable a);
	public void onAttachActions();
	public ArrayList<Attachable> getAttachables();
	public void updateAttachables();
}
