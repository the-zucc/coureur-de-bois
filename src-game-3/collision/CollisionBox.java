package collision;

import characteristic.positionnable.Collideable;
import game.Map;
import javafx.geometry.Point3D;

public abstract class CollisionBox {
	private Collideable collideable;
	public Collideable getCollideable() {
		return collideable;
	}

	private boolean tooBigForCollisionOptimization;
	protected Map map;
	public CollisionBox(Collideable collideable, Map map){
		this.tooBigForCollisionOptimization = isTooBigForCollisionOptimization();
		this.collideable = collideable;
		this.map = map;
	}
	public Point3D getCorrection(CollisionBox b1){
		return new Point3D(0,0,0);
	};
	public boolean collides(CollisionBox b1){
		if(b1 instanceof SphericalCollisionBox)
			return collidesSphericalBox((SphericalCollisionBox)b1);
		if(b1 instanceof CapsuleCollisionBox)
			return collidesCapsuleBox((CapsuleCollisionBox)b1);
		return false;
	}
	public abstract Point3D getCorrectionCapsuleBox(CapsuleCollisionBox b1);
	public abstract boolean collidesCapsuleBox(CapsuleCollisionBox b1);
	public abstract Point3D getCorrectionSphericalBox(SphericalCollisionBox b1);
	public abstract boolean collidesSphericalBox(SphericalCollisionBox b1);
	public abstract boolean movesOnCollision();
	public abstract double computeCollidingWeight();

	protected abstract boolean isTooBigForCollisionOptimization();

}
