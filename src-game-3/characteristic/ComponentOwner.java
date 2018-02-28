package characteristic;

import characteristic.positionnable.Positionnable;
import visual.Component;

public interface ComponentOwner extends Positionnable{
	/**
	 * should be used in the constructor of the implementing class to define the class' component attribute.
	 * once defined, the attribute should be accessed using getComponent()
	 * @return
	 */
	public Component buildComponent();
	/**
	 * in the implementing class, should return the component attribute of that class.
	 * @return the 3D component of the object
	 */
	public Component getComponent();
}
