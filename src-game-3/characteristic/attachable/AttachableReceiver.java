package characteristic.attachable;

import java.util.ArrayList;

import characteristic.ComponentOwner;
import characteristic.Messageable;
import characteristic.Updateable;
/**
 * interface to attach game components to other components. To see an example of an implementation,
 * see {@link entity.living.human.Player}.
 * @author Laurier
 */
public interface AttachableReceiver extends Updateable, ComponentOwner, Messageable{
	public void attach(Attachable a);
	public void detach(Attachable a);
	public void onAttachActions(Attachable a);
	public void onDetachActions(Attachable a);
	public ArrayList<Attachable> getAttachables();
	public void updateAttachables();
}
