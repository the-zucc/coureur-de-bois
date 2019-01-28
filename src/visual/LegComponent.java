package visual;

import javafx.scene.Scene;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class LegComponent extends Component {
	private Box leg;
	private double count;
	private double travelWidth;
	private double travelDepth;
	public LegComponent(String id,double d, double travelWidth, double travelDepth, double[] size, PhongMaterial material) {
		super(id);
		count = d;
		if(size.length < 3) {
			System.out.println("BOX SIZE ARRAY INCORRECT. expected minimum array size: 3. got "+size.length);
		}
		leg = new Box(size[0],size[1],size[2]);
		if(material != null) {
			leg.setMaterial(material);
		}
		this.travelDepth = travelDepth;
		this.travelWidth = travelWidth;
		getChildren().add(leg);
	}
	public void update() {
		count+=0.25;
		this.leg.setTranslateZ(travelDepth*Math.sin(count));
	}

	public Box getLeg() {
		return leg;
	}
}
