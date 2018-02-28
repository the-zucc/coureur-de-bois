package collision;

import javafx.geometry.Point3D;

public class CapsuleCollisionBox extends CollisionBox {

	@Override
	public Point3D getCorrectionSphericalBox(SphericalCollisionBox b1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean collidesSphericalBox(SphericalCollisionBox b1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Point3D getCorrectionCapsuleBox(CapsuleCollisionBox b1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean collidesCapsuleBox(CapsuleCollisionBox b1) {
		// TODO Auto-generated method stub
		return false;
	}

}
