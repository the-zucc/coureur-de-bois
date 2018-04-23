package visual;

import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import ui.gamescene.GameCamera;

import java.util.ArrayList;
import java.util.Hashtable;

public class Utils3D {
	private Hashtable<Node, ArrayList<Transform>> transforms;
	public static void lookat(Node node, Point3D lookatPosition) {
		Point3D nodePosition = Point3D.ZERO;
		Point3D lookat = node.sceneToLocal(lookatPosition);

		double distX = nodePosition.getX()-lookat.getX();
		double distY = nodePosition.getY()-lookat.getY();
		double distZ = nodePosition.getZ()-lookat.getZ();
		
		//hypothenuse du triange au sol, utilisé pour la rotation autour de l'axe des X
		double angleY = Math.toDegrees(Math.atan(distZ/distX));
		double hypot = Math.hypot(distX, distZ);
		double angleX = Math.toDegrees(Math.atan(hypot/Math.abs(distY)));
		Rotate ry = new Rotate(angleY, Rotate.Y_AXIS);
		Rotate rx = new Rotate(-angleX, Rotate.X_AXIS);
		for (int i = 0; i < node.getTransforms().size(); i++) {
			Transform t = node.getTransforms().get(i);
			if(t instanceof Rotate){
				//node.getTransforms().remove(t);
				//i--;
			}
		}
        node.getTransforms().add(ry);
        node.getTransforms().add(rx);

	}
}
