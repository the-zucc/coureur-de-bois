package entity.statics.village;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.statics.StaticVisibleCollidingEntity;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import util.MeshFactory;
import visual.Component;

public class Tipi extends StaticVisibleCollidingEntity {

    private double radius;
    public double getRadius(){
        return radius;
    }
    private double height;
    public double getHeight(){
        return height;
    }

    public Tipi(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
        this.radius = 100;
        this.height = 175;
    }

    @Override
    public Component buildComponent() {
        Component returnVal = new Component(getId());
        returnVal.getChildren().add(MeshFactory.buildRegularPyramid(6, 175, 100));
        return returnVal;
    }

    @Override
    public CollisionBox buildCollisionBox() {
        return null;
    }

    @Override
    public void onCollides(Collideable c) {

    }

    @Override
    public Point3D getAllCorrections() {
        return null;
    }

    @Override
	public double computeCollidingWeight() {
		return 0;
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}

	@Override
	public void onHover(MouseEvent me) {
		
	}

	@Override
	public void onUnHover(MouseEvent me) {
		
	}

	@Override
	protected String getMouseToolTipText() {
		return "tipi";
	}
}
