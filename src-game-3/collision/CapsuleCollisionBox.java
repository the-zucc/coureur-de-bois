package collision;

import characteristic.positionnable.Collideable;
import game.Map;
import javafx.geometry.Point3D;

public class CapsuleCollisionBox extends CollisionBox {
	

	public CapsuleCollisionBox(Collideable collideable, Map map) {
		super(collideable, map);
	}
	private Point3D p1;
	public Point3D getP1() {
		return p1;
	}
	
	private Point3D p2;
	public Point3D getP2(){
		return p2;
	}
	public void setP2(Point3D p2){
		if(p2.distance(p1) > this.p2.distance(p1)){
			
		}
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
		
		return false;
	}

	@Override
	public void update() {
		
	}

}
