package entity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;
import java.util.Iterator;

import app.App;
import characteristic.ComponentOwner;
import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.interactive.Hoverable;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.Positionnable2D;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import util.MessageCallback;
import visual.Component;

public abstract class VisibleEntity extends Entity implements ComponentOwner, MessageReceiver{
	
	private Component component;
	protected Point3D position;
	protected Point2D position2D;
	protected Messenger messenger;
	public VisibleEntity(Point3D position, Messenger messenger){
		super();
		this.messenger = messenger;
		listenTo(messenger);
		setPosition(position);
		component = buildComponent();
		component.setTranslateX(position.getX());
		component.setTranslateY(position.getY());
		component.setTranslateZ(position.getZ());
		component.setCursor(getHoveredCursor());
		component.setOnMouseEntered((e)->{
			this.onHover(e);
			App.getUserInterface().getGameScreen().setMouseTooltipText(getMouseToolTipText());
		});
		component.setOnMouseExited((e)->{
			App.getUserInterface().getGameScreen().setMouseTooltipText("");
			this.onUnHover(e);
		});
		component.setOnMouseClicked((e)->{
			if(e.getButton() == MouseButton.PRIMARY) {
				Parent pane = buildOnClickedPane();
				if(pane != null) {
					pane.setTranslateX(e.getSceneX());
					pane.setTranslateY(e.getSceneY());
					App.getUserInterface().getGameScreen().getUiRoot().getChildren().add(pane);
					Node closerNode = getPaneDismissNode(pane);
					if(closerNode != null) {
						closerNode.setOnMouseClicked((e2)->{
							App.getUserInterface().getGameScreen().getUiRoot().getChildren().remove(pane);
						});
					}
					this.onClick(e);
				}
			}
			else if(e.getButton() == MouseButton.SECONDARY){
				messenger.send("right_clicked", this);
			}
		});
	}
	
	protected abstract Cursor getHoveredCursor();
	protected abstract String getMouseToolTipText();
	@Override
	public void setPosition(Point3D position) {
		this.position = position;
		set2DPosition(compute2DPosition(position));
	}

	@Override
	public Point3D getPosition() {
		return this.position;
	}

	@Override
	public abstract Component buildComponent();
	
	@Override
	public Component getComponent(){
		return component;
	};

	@Override
	public boolean isComponentInScene() {
		return getComponent().getParent() != null;
	}

	@Override
	public void placeComponentInScene() {
		Map.getInstance().getComponent().addChildComponent(getComponent());
		getComponent().setTranslateX(getPosition().getX());
		getComponent().setTranslateY(getPosition().getY());
		getComponent().setTranslateZ(getPosition().getZ());
	}
	
	public Point2D compute2DPosition(Point3D position3D){
		return new Point2D(position3D.getX(), position3D.getZ());
	}
	public void set2DPosition(Point2D position2D){
		this.position2D = position2D;
	}
	public Point2D get2DPosition(){
		return position2D;
	}
	public double distanceFrom(Positionnable2D arg0){
		return position2D.distance(arg0.get2DPosition());
	}
	/**
	 * builds the pane that will be shown when clicking on the entity's component
	 * @return the JavaFX pane that will be show to the user
	 */
	protected abstract Parent buildOnClickedPane();
	/**
	 * should be defined in relation to {@link buildOnClickedPane()}. returns the element that has to be clicked by the
	 * user in order to dismiss the pane after clicking on the entity.
	 * @param the pane build by buildOnClickedPane() 
	 * @return the Node that has to be clicked to dismiss the pane.
	 */
	protected abstract Node getPaneDismissNode(Parent onClickedPane);
	/**
	 * Section pour Messenger
	**/
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
		try{
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
					getCallbackQueue().get(key).remove(params);
				}
				iterator.remove();
				getCallbackQueue().remove(key);
			}
		}catch(ConcurrentModificationException cme){
			processCallbackQueue();
			return;
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
			if(callbackQueue.containsKey(message)) {
				callbackQueue.get(message).add(null);
			}
			else {
				callbackQueue.put(message, new ArrayList<Object[]>());
			}
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
	public void listenTo(Messenger messenger){
		getMessengers().add(messenger);
		messenger.addReceiver(this);
	}
}
