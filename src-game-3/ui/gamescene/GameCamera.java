package ui.gamescene;

import java.util.Hashtable;

import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import characteristic.positionnable.Positionnable2D;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.transform.Rotate;
import visual.Component;

public class GameCamera extends PerspectiveCamera implements Attachable{

	private Component component;
	private double dist;
	private AttachableReceiver receiver;
	private Point3D relativePosition;
	private Point3D position;
	public GameCamera(double dist, AttachableReceiver ar){
		super(true);
		
		position=null;
		this.dist = dist;

		component = buildComponent();

		setNearClip(10);
		setFarClip(200000);

		double ty= -dist/3;
		component.setTranslateY(ty);
		double tz = -dist;
		component.setTranslateZ(tz);

		component.setRotationAxis(Rotate.X_AXIS);
		component.setRotate(-17);

		this.relativePosition = new Point3D(0,ty, tz);
		ar.attach(this);
	}

	@Override
	public void update(double secondsPassed) {
		position = computeAbsolutePosition();
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}

	@Override
	public void onMessageReceived(Hashtable<String, ? extends Object> message) {

	}

	@Override
	public void onAttach(AttachableReceiver ar) {
		getComponent().setTranslateX(relativePosition.getX());
		getComponent().setTranslateY(relativePosition.getY());
		getComponent().setTranslateZ(relativePosition.getZ());
	}
	@Override
	public void onDetach(AttachableReceiver ar) {
		position = computeAbsolutePosition();
		if(ar == getReceiver()){
            receiver.getComponent().removeChildComponent(getComponent());
            receiver = null;
            Map.getInstance().getComponent().addChildComponent(getComponent());
        }
	}

    @Override
	public void setPosition(Point3D position) {
    	
	}

	@Override
	public Point3D getPosition() {
		if(receiver != null)
			return relativePosition.add(receiver.getPosition());
		return position;
	}

	@Override
	public Point3D getPositionRelativeToReceiver() {
		return relativePosition;
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component("id_game_camera");
		returnVal.getChildren().add(this);
		return returnVal;
	}

	@Override
	public Component getComponent() {
		return component;
	}

	@Override
	public boolean isComponentInScene() {
		return getComponent().getParent() != null;
	}

	@Override
	public void placeComponentInScene() {
		getReceiver().attach(this);
	}

	@Override
	public AttachableReceiver getReceiver() {
		return receiver;
	}

	@Override
	public Point3D computeAbsolutePosition() {
		if(getReceiver() instanceof Attachable){
			return ((Attachable)getReceiver()).computeAbsolutePosition().add(relativePosition);
		}
		return getReceiver().getPosition().add(relativePosition);
	}

	@Override
	public Point2D compute2DPosition(Point3D position3d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set2DPosition(Point2D position2d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point2D get2DPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double distanceFrom(Positionnable2D arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
