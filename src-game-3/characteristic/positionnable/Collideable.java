package characteristic.positionnable;

import characteristic.Updateable;

public interface Collideable extends Positionnable, Updateable, Moveable{
	public CollisionBox buildCollisionBox();
}
