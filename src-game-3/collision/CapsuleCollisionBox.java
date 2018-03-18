package collision;

import javafx.geometry.Point3D;

public class CapsuleCollisionBox extends CollisionBox {
	private Point3D p1;
	public Point3D getP1() {
		return p1;
	}
	private Point3D p2;
	public Point3D getP2(){
		return p2;
	}


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
	public boolean movesOnCollision() {
		return false;
	}

	@Override
	public double computeCollidingWeight() {
		return 0;
	}

	@Override
	protected boolean isTooBigForCollisionOptimization() {
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
