package collision;

import javafx.geometry.Point3D;

public class CollisionMath {
	public static boolean testSphereSphere(SphericalCollisionBox box1, SphericalCollisionBox box2) {
		return Math.hypot(Math.hypot(box2.getPosition().getX()-box1.getPosition().getX(), box2.getPosition().getZ()-box1.getPosition().getZ()),box2.getPosition().getY()-box1.getPosition().getY()) < (box2.getRadius()+box1.getRadius());
	}
	public static Point3D correctSphereSphere(SphericalCollisionBox box1, SphericalCollisionBox box2) {
		double distance = Math.hypot(box2.getPosition().getX()-box1.getPosition().getX(), box2.getPosition().getZ()-box1.getPosition().getZ());
		double magnitude = distance - (box2.getRadius()+box1.getRadius());
		double frac = magnitude/distance;
		return new Point3D(frac*(box2.getPosition().getX()-box1.getPosition().getX()), frac*(box2.getPosition().getY()-box1.getPosition().getY()), frac*(box2.getPosition().getZ()-box1.getPosition().getZ()));
	}
	
	//capsule functions
	
	public static double squareDistPointCSegmentAB(Point3D a, Point3D b, Point3D c) {
		Point3D ab = b.subtract(a);
		Point3D ac = c.subtract(a);
		Point3D bc = c.subtract(b);
		
		double e = ab.dotProduct(ac);
		if(e <= 0.0) return ac.dotProduct(ac);
		
		double f = ab.dotProduct(ab);
		if(e >= f) return bc.dotProduct(bc);
		
		return ac.dotProduct(ac)-e*(e/f);
	}
	
	
	public static boolean testSphereCapsule(SphericalCollisionBox box1, CapsuleCollisionBox box2) {
		double combinedRadiusSquared = Math.pow((box1.getRadius()+box2.getRadius()),2);
		return combinedRadiusSquared > squareDistPointCSegmentAB(box2.getP1(), box2.getP2(), box1.getPosition());
	}
	public boolean testCapsuleSphere(SphericalCollisionBox box1, CapsuleCollisionBox box2) {
		return false;
	}
	
	public static Point3D correctSphereCapsule() {
		
		return null;
	}
	
	public static Point3D[] correctCapsuleSphere(CapsuleCollisionBox box1, SphericalCollisionBox box2) {
		
		return null;
	}
}
