package characteristic.positionnable;

import characteristic.Updateable;
import collision.CollisionBox;
import javafx.geometry.Point3D;

public interface Collideable extends Positionnable, Updateable, Moveable{
	public CollisionBox buildCollisionBox();
	public CollisionBox getCollisionBox();
	public void onCollides(Collideable c);
	public boolean collides(Collideable c);
	public Point3D getCorrection(Collideable c);
}
