package ui.gamescene;

import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import characteristic.positionnable.Positionnable;
import characteristic.positionnable.Positionnable2D;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import visual.Component;

public class GameCamera extends PerspectiveCamera implements Attachable{

	private Component component;
	private float dist;
	private AttachableReceiver receiver;
	private Point3D relativePosition;
	private Point3D position;
	private Point3D speeds;
	private Point3D accelX;
	private Point3D accelY;
	private Point3D accelZ;
	private Map map;
	private Point2D position2D;
	private float angle;
	
	public GameCamera(float dist, AttachableReceiver ar, Map map){
		super(true);
		
		position=null;
		
		this.dist = dist;
		this.map = map;
		
		component = buildComponent();

		setNearClip(10);
		setFarClip(20000);

		double ty= -dist;
		//component.setTranslateY(ty);
		double tz = -dist;
		//component.setTranslateZ(tz);
		this.angle = -45;
		//component.setRotationAxis(Rotate.X_AXIS);
		//component.setRotate(angle);
		
		double accel = 0.1;
		speeds = Point3D.ZERO;
		accelY = new Point3D(0,-accel,0);
		
		this.relativePosition = new Point3D(0,ty, tz);
		ar.attach(this);
	}

	@Override
	public void update(float secondsPassed) {
		/*
		Point3D position = localToScene(Point3D.ZERO);
		double heightParam = 5*GameLogic.getMeterLength();
		double desiredCameraHeight = map.getHeightAt(new Point2D(position.getX(), position.getZ()))-heightParam;
		double height = component.getParent().sceneToLocal(new Point3D(0,desiredCameraHeight,0)).getY();
		if(position.getY() > desiredCameraHeight) {
			speeds = speeds.add(accelY);
			//aller vers le bas
			if(speeds.getY() > 0 && Math.abs(position.getY()-desiredCameraHeight)<Math.abs(speeds.getY())) {
				speeds = new Point3D(speeds.getX(),0,speeds.getZ());
			}
		}
		else if(position.getY() < desiredCameraHeight) {
			speeds = speeds.subtract(accelY);
			//aller vers le haut
			if(speeds.getY() < 0 && Math.abs(position.getY()-desiredCameraHeight)<Math.abs(speeds.getY())) {
				speeds = new Point3D(speeds.getX(),0,speeds.getZ());
			}
		}
		relativePosition = relativePosition.add(speeds);

		component.setTranslateX(relativePosition.getX());
		component.setTranslateY(relativePosition.getY());
		component.setTranslateZ(relativePosition.getZ());
		
		Utils3D.lookat(getComponent(), map.getCurrentPlayer().getPosition());
		*/
		getComponent().setTranslateX(relativePosition.getX());
		getComponent().setTranslateY(relativePosition.getY());
		getComponent().setTranslateZ(relativePosition.getZ());
		getComponent().setRotationAxis(Rotate.X_AXIS);
		getComponent().setRotate(angle);
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}

    @Override
	public void onAttach(AttachableReceiver ar) {
    	this.setAngle(angle);
		getComponent().setTranslateX(relativePosition.getX());
		getComponent().setTranslateY(relativePosition.getY());
		getComponent().setTranslateZ(relativePosition.getZ());
	}
	@Override
	public void onDetach(AttachableReceiver ar) {
		//this.relativePosition = computeAbsolutePosition();
		this.relativePosition = getComponent().localToScene(relativePosition);
		if(ar == getReceiver()){
            receiver.getComponent().removeChildComponent(getComponent());
            receiver = null;
            Map.getMainMap().getComponent().addChildComponent(getComponent());
        }
	}

    @Override
	public void setPosition(Point3D position) {
    	this.position = position;
    	this.position2D = compute2DPosition(position);
	}

	@Override
	public Point3D getPosition() {
		if(receiver != null)
			return relativePosition.add(receiver.getPosition());
		return position;
	}

	@Override
	public float distanceFrom(Positionnable p) {
		return (float)p.getPosition().distance(this.getPosition());
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
		return localToScene(Point3D.ZERO);
	}

	@Override
	public Point2D compute2DPosition(Point3D position3d) {
		return new Point2D(position3d.getX(), position3d.getZ());
	}

	@Override
	public void set2DPosition(Point2D position2d) {
		this.position2D = position2d;
	}

	@Override
	public Point2D get2DPosition() {
		return position2D;
	}

	@Override
	public float distance2DFrom(Positionnable2D arg0) {
		
		return (float)get2DPosition().distance(arg0.get2DPosition());
	}

	@Override
	public void onHover(MouseEvent me) {
		
	}

	@Override
	public void onUnHover(MouseEvent me) {
		
	}

	@Override
	public void onClick(MouseEvent me) {
		
	}
	public void setAngle(float angle) {
		this.angle = angle;
		double y = -Math.cos(Math.toRadians(-90-angle))*this.dist;
		double z = Math.sin(Math.toRadians(-90-angle))*this.dist;
		this.relativePosition = new Point3D(0, y, z);
	}

	public float getAngle() {
		return angle;
	}
}
