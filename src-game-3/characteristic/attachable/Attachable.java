package characteristic.attachable;

import characteristic.Updateable;
import javafx.geometry.Point3D;
import characteristic.ComponentOwner;

public interface Attachable extends Updateable, ComponentOwner {
	public void onAttach(AttachableReceiver ar);
	public void onDetach(AttachableReceiver ar);
	public AttachableReceiver getReceiver();
	public Point3D getPositionRelativeToReceiver();
	public Point3D computeAbsolutePosition();
}
