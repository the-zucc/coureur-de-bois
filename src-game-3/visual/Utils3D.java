package visual;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;

public class Utils3D {
	public static void lookat(Node node, Point3D lookatPosition) {
		Point3D nodePosition = node.localToScene(Point3D.ZERO);
		double distX = lookatPosition.getX()-nodePosition.getX();
		double distY = lookatPosition.getY()-nodePosition.getY();
		double distZ = lookatPosition.getZ()-nodePosition.getZ();
		
		//hypothenuse du triange au sol, utilisé pour la rotation autour de l'axe des X
		double angleY = distZ > 0 ? 90-Math.toDegrees(Math.atan(distZ/distX)) : 90;
		double hypot = Math.hypot(distX, distZ);
		double angleX = Math.toDegrees(Math.atan(hypot/distY));
		node.getTransforms().add(new Rotate(angleY, Rotate.Y_AXIS));
	}
}
