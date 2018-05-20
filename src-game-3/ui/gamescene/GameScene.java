package ui.gamescene;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;
import java.util.Iterator;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.Updateable;
import game.GameLogic;
import game.Map;
import game.settings.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import util.MessageCallback;
import visual.Utils3D;

public class GameScene extends SubScene implements MessageReceiver, Updateable{
	private Group gameRoot;
	private GameCamera gameCamera;
	private Messenger messenger;
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	private Map map;
	public GameScene(double arg1, double arg2, Stage window, Map map, Messenger messenger) {
		super(new Group(), arg1, arg2, true, Settings.getAntialiasingValue());
		this.map = map;
		this.messenger = messenger;
		gameRoot = (Group)getRoot();
		gameCamera = new GameCamera(20*GameLogic.getMeterLength(), Map.getInstance().getCurrentPlayer(), map);
		setCamera(gameCamera);
		//Utils3D.lookat(gameCamera, map.getCurrentPlayer().getPosition());
		window.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				GameScene.this.setWidth(newValue.doubleValue());
			}
		});
		window.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				GameScene.this.setHeight(newValue.doubleValue());
			}
		});
		
		setOnMouseClicked((e)->{
			if(e.getButton() == MouseButton.SECONDARY){
				messenger.send("right_clicked");
			}
		});
		setOnMouseMoved((e)->{
			messenger.send("mouse_moved", e);
		});
	}
	public Group getGameRoot(){
		return gameRoot;
	}
	public void createConnection(Point3D origin, Point3D target, PhongMaterial material) {
		Point3D yAxis = new Point3D(0, 1, 0);
		Point3D diff = target.subtract(origin);
		double height = diff.magnitude();

		Point3D mid = target.midpoint(origin);
		Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

		Point3D axisOfRotation = diff.crossProduct(yAxis);
		double angle = Math.acos(diff.normalize().dotProduct(yAxis));
		Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

		Cylinder line = new Cylinder(5, height);

		line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
		line.setMaterial(material);

		((Group)this.getRoot()).getChildren().addAll(line);
	}
	/**
	 * For messenger
	 */
	/**
	 * Section pour Messenger
	**/
	private ArrayList<Messenger> messengers = new ArrayList<Messenger>();
	@Override
	public ArrayList<Messenger> getMessengers(){
		return messengers;
	}

	@Override
	public void processCallbackQueue(){

		try{
			ArrayList<String> receivedMessages = getReceivedMessages();
			ArrayList<Object[]> receivedParams = getReceivedParams();
			while(!receivedMessages.isEmpty()) {
				getAccepts().get(receivedMessages.get(0)).run(receivedParams.get(0));
				receivedMessages.remove(0);
				receivedParams.remove(0);
			}
		}catch(ConcurrentModificationException cme){
			processCallbackQueue();
			return;
		}
	}
	@Override
	public void receiveMessage(String message, Object... params){
		getReceivedMessages().add(message);
		getReceivedParams().add(params);
	}

	@Override
	public void receiveMessage(String message){
		getReceivedMessages().add(message);
		getReceivedParams().add(null);
	}

	@Override
	public void accept(String message, MessageCallback callback){
		getAccepts().put(message, callback);
		messenger.setupListener(message, this);
	}
	
	Hashtable<String, MessageCallback> accepts = new Hashtable<String, MessageCallback>();
	@Override
	public Hashtable<String, MessageCallback> getAccepts(){
		return accepts;
	}
	
	@Override
	public void listenTo(Messenger messenger){
		getMessengers().add(messenger);
		messenger.addReceiver(this);
	}
	
	ArrayList<String>  receivedMessages = new ArrayList<String>();
	@Override
	public ArrayList<String> getReceivedMessages(){
		return receivedMessages;
	}
	
	ArrayList<Object[]> receivedParams = new ArrayList<Object[]>();
	@Override
	public ArrayList<Object[]> getReceivedParams(){
		return receivedParams;
	}
	private ArrayList<Box> snowFlakes = new ArrayList<Box>();
	@Override
	public void update(double secondsPassed) {
		double meter = GameLogic.getMeterLength();
		Point2D playerPos = map.getCurrentPlayer().get2DPosition();
		double playerHeight = map.getCurrentPlayer().getPosition().getY();
		double widthInMeters = 25;
		for(int i = 0; i < 3; i++) {
			Box r = new Box(0.25,0.25,0.25);
			r.setOpacity(2);
			double x = (Math.random()*widthInMeters*meter)-(widthInMeters/2)*meter + playerPos.getX();
			double y = (Math.random()*widthInMeters*meter)-(widthInMeters/2)*meter + playerPos.getY();
			Point2D pos = new Point2D(x,y);
			r.setTranslateX(pos.getX());
			r.setTranslateZ(pos.getY());
			r.setMaterial(new PhongMaterial(Color.WHITE));
			r.setTranslateY(playerHeight-20*GameLogic.getMeterLength());
			snowFlakes.add(r);
			gameRoot.getChildren().add(r);
		}
		
		for(int i = 0; i < snowFlakes.size(); i++) {
			Box r2 = snowFlakes.get(i);
			
			gameRoot.getChildren().remove(snowFlakes.get(0));
			if(r2.getTranslateY() > map.getHeightAt(new Point2D(r2.getTranslateX(), r2.getTranslateZ()))) {
				snowFlakes.remove(r2);				
				gameRoot.getChildren().remove(r2);
			}
			else {
				r2.setTranslateY(r2.getTranslateY()+GameLogic.getMeterLength()*0.5);				
			}
			//System.out.println(snowFlakes.get(i).getTranslateY());
		}
	}
	@Override
	public boolean shouldUpdate() {
		return true;
	}
}
