package collision;

import javafx.geometry.Point3D;

public abstract class CollisionBox {
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
}
