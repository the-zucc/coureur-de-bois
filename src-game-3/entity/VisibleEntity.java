package entity;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;
import java.util.Iterator;

import animator.Animator;
import app.App;
import characteristic.Animatable;
import characteristic.ComponentOwner;
import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.Updateable;
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
import javafx.scene.input.MouseEvent;
import util.MessageCallback;
import util.StopCondition;
import visual.Component;

public abstract class VisibleEntity extends Entity implements Updateable, ComponentOwner, MessageReceiver, Animatable{
	protected Animator animator;
	private ArrayList<Runnable> additionnalUpdates = new ArrayList<Runnable>();
	private ArrayList<StopCondition> additionnalUpdatesStopConditions = new ArrayList<StopCondition>();
	private ArrayList<Runnable> additionnalUpdatesCallbacks = new ArrayList<Runnable>();
	protected void addUpdate(Runnable job, StopCondition condition, Runnable callback) {
		additionnalUpdates.add(job);
		additionnalUpdatesStopConditions.add(condition);
		additionnalUpdatesCallbacks.add(callback);
	}
	private void processUpdates() {
		for(int i = 0; i < additionnalUpdates.size(); i++) {
			additionnalUpdates.get(i).run();
			if(additionnalUpdatesStopConditions.get(i).shouldStop()) {
				additionnalUpdates.remove(i);
				additionnalUpdatesStopConditions.remove(i);
				additionnalUpdatesCallbacks.get(i).run();
				additionnalUpdatesCallbacks.remove(i);
				i--;
			}
		}
	}
	private Component component;
	protected Point3D position;
	protected Point2D position2D;
	protected Messenger messenger;
	protected Map map;
	public VisibleEntity(Point3D position, Map map, Messenger messenger){
		super();
		this.map = map;
		this.animator = new Animator();
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
	
	private int ticksToGrow = 8;
	@Override
	public void onHover(MouseEvent me) {
		animator.animate(()->{
			double scale = getComponent().getScaleX();
			getComponent().setScaleX(scale*1.015);
			getComponent().setScaleY(scale*1.015);
			getComponent().setScaleZ(scale*1.015);
		}, ticksToGrow);
	}
	
	@Override
	public void onUnHover(MouseEvent me) {
		animator.animate(()->{
			double scale = getComponent().getScaleX();
			getComponent().setScaleX(scale/1.02);
			getComponent().setScaleY(scale/1.02);
			getComponent().setScaleZ(scale/1.02);
		}, ticksToGrow);
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
	
	public Animator getAnimator() {
		return animator;
	}
	
	@Override
	public void update(double secondsPassed) {
		animator.playAnimations();
		processCallbackQueue();
		processUpdates();
	}
	
}
