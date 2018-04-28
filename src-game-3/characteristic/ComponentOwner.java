package characteristic;

import characteristic.interactive.Hoverable;
import characteristic.positionnable.Positionnable;
import visual.Component;

public interface ComponentOwner extends Positionnable, Hoverable{
	/**
	 * should be used in the constructor of the implementing class to define the class' component attribute.
	 * once defined, the attribute should be accessed using getComponent().
	 * @return the constructed 3D Component
	 */
	public Component buildComponent();
	/**
	 * in the implementing class, should return the component attribute of that class.
	 * @return the 3D component of the object
	 */
	public Component getComponent();
	
	/**
	 * in the implementing class, should return whether or not the component is positioned in the scene (this is done by checking if the node has a parent).
	 * @return if the component has a parent
	 */
	public boolean isComponentInScene();
	/**
	 * in the implementing class, this method should place the component of the entity in the scene. should ideally only be used once, when the component has just been created and the method isComponentInScene() returns false.
	 */
	public void placeComponentInScene();
}
