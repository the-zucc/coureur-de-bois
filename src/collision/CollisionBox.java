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
	public boolean isBigCollisionBox(){
		return tooBigForCollisionOptimization;
	}
	protected Map map;
	public void setMap(Map map){
	    this.map = map;
    }
	public CollisionBox(Collideable collideable, Map map){
		this.tooBigForCollisionOptimization = isTooBigForCollisionOptimization();
		this.collideable = collideable;
		this.map = map;
	}
	public Point3D getCorrection(CollisionBox b1){
		if(b1 instanceof SphericalCollisionBox){
			return getCorrectionSphericalBox((SphericalCollisionBox)b1);
		}
		else{
			return new Point3D(0,0,0);
		}

	};
	public boolean collides(CollisionBox b1){
		try {
			if(b1 instanceof SphericalCollisionBox)
				return collidesSphericalBox((SphericalCollisionBox)b1);			
		}catch(Exception e) {
			
		}
		return false;
	}
	public abstract Point3D getCorrectionSphericalBox(SphericalCollisionBox b1);
	public abstract boolean collidesSphericalBox(SphericalCollisionBox b1);
	public abstract boolean movesOnCollision();
	public abstract float computeCollidingWeight();

	protected abstract boolean isTooBigForCollisionOptimization();
	public abstract void update();
}
