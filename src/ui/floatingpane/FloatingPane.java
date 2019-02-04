package ui.floatingpane;

import java.util.Hashtable;

import characteristic.positionnable.Positionnable2D;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import ui.gamescene.GameScreen;

public abstract class FloatingPane extends Group implements Positionnable2D {
	
	private Point2D position2D;
	private double height;
	private double width;
	private GameScreen screen;
	
	public FloatingPane(GameScreen screen){
		this.screen = screen;
		width = computeWidth();
		height = computeHeight();
	}
	
	public void hide(){
		
	}

    protected abstract void onMessageReceived_sub(Hashtable<String, ? extends Object> message);
	protected abstract void update(Hashtable<String, ? extends Object> data);
	protected abstract double computeWidth();
	protected abstract double computeHeight();
	
	@Override
	public Point2D compute2DPosition(Point3D position3d) {
		return null;
	}
	@Override
	public void set2DPosition(Point2D position2d) {
		position2D = position2d;
		setTranslateX(position2D.getX()-width/2);
		setTranslateY(position2D.getY()-height/2);
	}
	@Override
	public Point2D get2DPosition() {
		return position2D;
	}
	@Override
	public float distance2DFrom(Positionnable2D arg0) {
		return (float)arg0.get2DPosition().distance(get2DPosition());
	}
	
}
