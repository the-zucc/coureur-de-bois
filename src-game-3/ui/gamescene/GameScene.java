package ui.gamescene;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import characteristic.MessageReceiver;
import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import game.settings.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import util.MessageCallback;
import visual.Utils3D;

public class GameScene extends SubScene implements MessageReceiver {
	private Group gameRoot;
	private GameCamera gameCamera;
	private Messenger messenger;
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	public GameScene(double arg1, double arg2, Stage window, Map map, Messenger messenger) {
		super(new Group(), arg1, arg2, true, Settings.getAntialiasingValue());
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
		/*
		setOnMouseClicked((e)->{
			if(e.getButton() == MouseButton.SECONDARY){
				messenger.send("right_clicked");
			}
		});
		*/
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
	private ArrayList<Messenger> messengers = new ArrayList<Messenger>();
	@Override
	public ArrayList<Messenger> getMessengers(){
		return messengers;
	}
	
	Hashtable<String, ArrayList<Object[]>> callbackQueue = new Hashtable<String, ArrayList<Object[]>>();
	@Override
	public Hashtable<String, ArrayList<Object[]>> getCallbackQueue(){
		return callbackQueue;
	}

	@Override
	public void processCallbackQueue(){
		Iterator<String> iterator = callbackQueue.keySet().iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			ArrayList<Object[]> paramsList = getCallbackQueue().get(key);
			for(Object[] params:paramsList){
				if(params != null){
					getAccepts().get(key).run(params);
				}
				else{
					getAccepts().get(key).run();
				}
			}
		}
		callbackQueue.clear();
	}
	@Override
	public void receiveMessage(String message, Object... params){
		if(getAccepts().containsKey(message)){
			if(!getCallbackQueue().containsKey(message)){
				ArrayList<Object[]> paramsArray = new ArrayList<Object[]>();
				paramsArray.add(params);
				getCallbackQueue().put(message, paramsArray);
			}
			else{
				getCallbackQueue().get(message).add(params);
			}
		}
	}

	@Override
	public void receiveMessage(String message){
		if(getAccepts().containsKey(message)){
			callbackQueue.put(message, null);
		}
	}

	@Override
	public void accept(String message, MessageCallback callback){
		getAccepts().put(message, callback);
	}

	Hashtable<String, MessageCallback> accepts = new Hashtable<String, MessageCallback>();
	@Override
	public Hashtable<String, MessageCallback> getAccepts(){
		return accepts;
	}
	@Override
	public void listenTo(Messenger messenger) {
		getMessengers().add(messenger);
		messenger.addReceiver(this);
	}
}
