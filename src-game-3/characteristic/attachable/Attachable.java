package characteristic.attachable;

import characteristic.Updateable;
import characteristic.positionnable.Positionnable;
import javafx.geometry.Point3D;
import characteristic.ComponentOwner;
import characteristic.Messageable;

public interface Attachable extends Updateable, ComponentOwner, Messageable{
	public void onAttach(AttachableReceiver ar);
	public void onDetach();
	public void detachFromReceiver(AttachableReceiver ar);
	public AttachableReceiver getReceiver();
	public Point3D getPositionRelativeToReceiver();
	public Point3D computeAbsolutePosition();
}
