package collision;

import javafx.geometry.Point3D;
import model.Model;

public abstract class CollisionBox {
	protected Point3D position;
	
	protected boolean tooBigForCollisionDetectionSystem;
	private int mapDivisionRow;
	private int mapDivisionColumn;
	
	public int getMapDivisionRow() {
		return mapDivisionRow;
	}
	
	public int getMapDivisionColumn() {
		return mapDivisionColumn;
	}
	
	public CollisionBox(Point3D position) {
		position = position;
	}

	public void setPosition(Point3D position) {
		this.position = position;
		if(!tooBigForCollisionDetectionSystem) {
			int newMapDivisionRow = ((int)(position.getZ()-Model.minCoordDebug))/CollisionMatrix.getMapDivisionHeight();
			int newMapDivisionColumn = ((int)(position.getX()-Model.minCoordDebug))/CollisionMatrix.getMapDivisionWidth();
			//if the collision box is in a different map division
			if(newMapDivisionRow != mapDivisionRow || newMapDivisionColumn != mapDivisionColumn) {
				//remove from the old division
				Model.getInstance().getFloorMatrix().removeFromDivision(mapDivisionRow, mapDivisionColumn, this);
				mapDivisionRow = newMapDivisionRow;
				mapDivisionColumn = newMapDivisionColumn;
				//add to the new division
				Model.getInstance().getFloorMatrix().addToDivision(mapDivisionRow, mapDivisionColumn, this);
				//System.out.println(mapDivisionRow+" "+mapDivisionColumn);
			}
		}
	}
	
	public Point3D getPosition() {
		return position;
	}
	
	public Point3D getCorrection(CollisionBox arg0) {
		if(arg0 instanceof CircularCollisionBox) {
			return getCorrectionCircularBox((CircularCollisionBox)arg0);
		}
		else if(arg0 instanceof RectangularCollisionBox) {
			return getCorrectionRectangularBox((RectangularCollisionBox)arg0);
		}
		else if(arg0 instanceof DiagonalCollisionBox) {
			return getCorrectionDiagonalBox((DiagonalCollisionBox)arg0);
		}
		return null;
	}
	
	public boolean collides(CollisionBox arg0) {
		if(arg0 instanceof CircularCollisionBox) {
			return collidesCircularBox((CircularCollisionBox)arg0);
		}
		else if(arg0 instanceof RectangularCollisionBox) {
			return collidesRectangularBox((RectangularCollisionBox)arg0);
		}
		else if(arg0 instanceof DiagonalCollisionBox) {
			return collidesDiagonalBox((DiagonalCollisionBox)arg0);
		}
		return false;
	}
	protected abstract boolean collidesCircularBox(CircularCollisionBox box);
	protected abstract boolean collidesRectangularBox(RectangularCollisionBox box);
	protected abstract boolean collidesDiagonalBox(DiagonalCollisionBox box);
	
	protected abstract Point3D getCorrectionCircularBox(CircularCollisionBox box);
	protected abstract Point3D getCorrectionRectangularBox(RectangularCollisionBox box);
	protected abstract Point3D getCorrectionDiagonalBox(DiagonalCollisionBox box);
}
