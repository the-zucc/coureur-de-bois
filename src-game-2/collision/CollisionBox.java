package collision;

import javafx.geometry.Point3D;
import app.Model;

public abstract class CollisionBox {
	protected Point3D position;
	
	protected boolean tooBigForCollisionDetectionSystem;
	private int mapDivisionRow;
	private int mapDivisionColumn;
	private String id;
	
	public int getMapDivisionRow() {
		return mapDivisionRow;
	}
	
	public int getMapDivisionColumn() {
		return mapDivisionColumn;
	}
	
	public CollisionBox(String id, Point3D position) {
		this.id = id;
		this.position = position;
	}
	
	public void setPosition(Point3D position) {
		this.position = position;
		if(!tooBigForCollisionDetectionSystem) {
			int newMapDivisionRow = ((int)(position.getZ()-(-Model.getInstance().getMapHeight()/2)))/CollisionGrid.getMapDivisionHeight();
			int newMapDivisionColumn = ((int)(position.getX()-(-Model.getInstance().getMapWidth()/2))/CollisionGrid.getMapDivisionWidth());
			//if the collision box is in a different map division
			if(newMapDivisionRow != mapDivisionRow || newMapDivisionColumn != mapDivisionColumn) {
				//remove from the old division
				CollisionGrid.getInstance().removeFromDivision(mapDivisionRow, mapDivisionColumn, this);
				mapDivisionRow = newMapDivisionRow;
				mapDivisionColumn = newMapDivisionColumn;
				//add to the new division
				CollisionGrid.getInstance().addToDivision(mapDivisionRow, mapDivisionColumn, this);
				//System.out.println(mapDivisionRow+" "+mapDivisionColumn);
			}
		}
	}
	
	public Point3D getPosition() {
		return position;
	}
	
	public Point3D getCorrection(CollisionBox arg0) {
		if(arg0 instanceof SphericalCollisionBox) {
			return getCorrectionSphericalBox((SphericalCollisionBox)arg0);
		}
		else if(arg0 instanceof RectangularCollisionBox) {
			return getCorrectionRectangularBox((RectangularCollisionBox)arg0);
		}
		else if(arg0 instanceof DiagonalCollisionBox) {
			return getCorrectionDiagonalBox((DiagonalCollisionBox)arg0);
		}
		else if(arg0 instanceof CylindricalCollisionBox) {
			return getCorrectionCylindricalBox((CylindricalCollisionBox)arg0);
		}
		return null;
	}
	
	

	public boolean collides(CollisionBox arg0) {
		if(arg0 instanceof SphericalCollisionBox) {
			return collidesSphericalBox((SphericalCollisionBox)arg0);
		}
		else if(arg0 instanceof RectangularCollisionBox) {
			return collidesRectangularBox((RectangularCollisionBox)arg0);
		}
		else if(arg0 instanceof DiagonalCollisionBox) {
			return collidesDiagonalBox((DiagonalCollisionBox)arg0);
		}
		else if(arg0 instanceof CylindricalCollisionBox) {
			return collidesCylindricalBox((CylindricalCollisionBox)arg0);
		}
		return false;
	}
	
	protected abstract boolean collidesSphericalBox(SphericalCollisionBox box);
	protected abstract boolean collidesRectangularBox(RectangularCollisionBox box);
	protected abstract boolean collidesDiagonalBox(DiagonalCollisionBox box);
	protected abstract boolean collidesCylindricalBox(CylindricalCollisionBox box);
	
	protected abstract Point3D getCorrectionSphericalBox(SphericalCollisionBox box);
	protected abstract Point3D getCorrectionRectangularBox(RectangularCollisionBox box);
	protected abstract Point3D getCorrectionDiagonalBox(DiagonalCollisionBox box);
	protected abstract Point3D getCorrectionCylindricalBox(CylindricalCollisionBox arg0);

	public String getId() {
		return id;
	}
}
