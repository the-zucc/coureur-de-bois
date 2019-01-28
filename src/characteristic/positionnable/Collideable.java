package characteristic.positionnable;

import characteristic.Updateable;
import collision.CollisionBox;
import javafx.geometry.Point3D;

public interface Collideable extends Positionnable, Moveable{
	public CollisionBox buildCollisionBox();
	public CollisionBox getCollisionBox();
	public void onCollides(Collideable c);
	public boolean collides(Collideable c);
	public Point3D getCorrection(Collideable c);
	public boolean canMoveOnCollision();
	public Point3D getAllCorrections();//should combine all of the corrections into one big vector which will be returned.
	public double computeCollidingWeight();
	int getCollisionMapRow();
	int getCollisionMapColumn();
}
