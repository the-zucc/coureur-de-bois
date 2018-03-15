package village;

import characteristic.positionnable.*;
import game.Map;
import javafx.geometry.Point3D;

public class Village implements Positionnable{
	Point3D position;
	
	public Village(Point3D position, int tipiCount, double radius, int tipiRowCount, int villagerCount) {
		int tipiCountPerRow = tipiCount;
		if(tipiCount > 6)
			tipiCountPerRow=6;
		double angleBetweenTipis = Math.toRadians(360/tipiCountPerRow);

	}
	
	@Override
	public void setPosition(Point3D position) {
		this.position = position;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}
	public void addEntitiesToMap(Map m) {
		
	}
}
