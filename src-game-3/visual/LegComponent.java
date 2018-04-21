package visual;

import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

public class LegComponent extends Component {
	private Box leg;
	private double count;
	private double travelWidth;
	private double travelHeight;
	public LegComponent(String id,int counter, double travelWidth, double travelDepth, int[] size, PhongMaterial material) {
		super(id);
		if(size.length < 3) {
			Exception e = new Exception("BOX SIZE INCORRECT. expected minimum size: 3 got "+size.length);
		}
		count = 0;
		leg = new Box(size[0],size[1],size[1]);
		if(material != null) {
			leg.setMaterial(material);
		}
		this.travelHeight = travelHeight;
		this.travelWidth = travelWidth;
	}
	public void update() {
		count++;
		this.leg.setTranslateX(travelWidth*Math.cos(count));
	}

}
