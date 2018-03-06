package ui.gamescene;

import java.util.Hashtable;

import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
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
		setNearClip(10);
		setFarClip(20000);
		setTranslateY(-dist);
		setTranslateZ(-dist);
		setRotationAxis(Rotate.X_AXIS);
		setRotate(-45);
		ar.attach(this);
	}

	@Override
	public void update(double secondsPassed) {
		position = computeAbsolutePosition();
	}

	@Override
	public boolean shouldUpdate() {
		return ;
	}

	@Override
	public void onMessageReceived(Hashtable<String, ? extends Object> message) {
		
	}

	@Override
	public void onAttach(AttachableReceiver ar) {
		ar.getComponent().addChildComponent(getComponent());
		this.relativePosition = new Point3D(0,-dist, -dist);
		getComponent().setTranslateX(relativePosition.getX());
		getComponent().setTranslateY(relativePosition.getY());
		getComponent().setTranslateZ(relativePosition.getZ());
	}
	@Override
	public void onDetach() {
		position = getReceiver().getPosition().add(relativePosition);
	}

	@Override
	public void detachFromReceiver(AttachableReceiver ar) {
		ar.detach(this);
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

}
