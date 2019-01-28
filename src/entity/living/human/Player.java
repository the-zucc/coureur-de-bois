package entity.living.human;

import app.App;
import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.attachable.Attachable;
import characteristic.attachable.AttachableReceiver;
import characteristic.interactive.UserControllable;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.Positionnable;
import characteristic.positionnable.Reachable;
import entity.living.LivingEntity;
import entity.statics.tree.Tree;
import game.GameLogic;
import game.Map;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import ui.gamescene.GameCamera;
import util.NodeUtils;
import visual.Component;
import entity.wearable.StandardSword;

import java.util.ArrayList;

public class Player extends Human implements UserControllable, AttachableReceiver{
	
	private static Point3D jumpVector = new Point3D(0, -20*GameLogic.getMeterLength(), 0);
	private GameCamera gameCamera;

	public Player(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger,  1);
		hp=300;
		maxHp = hp;
		accept("right_clicked", (params)->{
			if(params != null) {
				if(params.length > 0) {
					if(params[0] != this) {
						if(params[0] instanceof LivingEntity){
							if(wieldedWeapon != null) {
								if(!isDoingAction()) {
									this.startDoingAction();
									addUpdate(()->{
										this.startMovingTo(((LivingEntity)params[0]).get2DPosition());
										boolean hasWeapon = this.wieldedWeapon != null;
										double maxDist;
										if(hasWeapon && wieldedWeapon instanceof StandardSword) {
											maxDist = hasWeapon ? ((StandardSword)wieldedWeapon).getSwordLength()*1.5 : GameLogic.getMeterLength()*1.5;
										}
										else {
											maxDist = GameLogic.getMeterLength();
										}
										boolean checkDistance = Player.this.getPosition().distance(((LivingEntity)params[0]).getPosition()) < maxDist;
										if(checkDistance) {
											attack((LivingEntity)params[0],10);									
										}
									}, ()->{
										return ((LivingEntity)params[0]).getHp() <= 0;
									}, ()->{
										this.startDoingAction();
									});								
								}
							}
						}else if(params[0] instanceof Tree){
							goCutDownTree((Tree)params[0]);
						}else if(params[0]instanceof Positionnable){
							addUpdate(()->{
								this.startMovingTo(((Positionnable)params[0]).get2DPosition());							
							}, ()->{
								return this.distance2DFrom((Positionnable)params[0]) < GameLogic.getMeterLength();
							}, ()->{
								
							});
							
						}
					}
					else {
						attack(null, 10);
					}
				}
			}
		});
		accept("mouse_moved", (params)->{
			MouseEvent me = (MouseEvent)params[0];
			mouseX = me.getSceneX()-App.getUserInterface().getWidth()/2;
			mouseY = me.getSceneY()-App.getUserInterface().getHeight()/2;
		});
		accept("hp", (params)->{
			if(params[0] == this) {
				double hpBoost = (Double)params[1];
				takeDamage(-hpBoost, null);
			}
		});
		accept("max_hp", (params)->{
			if(params[0] == this) {
				double hpBoost = (Double)params[1];
				takeDamage(-hpBoost, null);
				maxHp = maxHp+hpBoost;
			}
		});
		accept("item_picked_up", (params)->{
			if(params[0] == this) {
				String key = params[1].getClass().getName();
				if(!items.containsKey(key)) {
					items.put(key,1);
				}else {
					items.put(key,items.get(key)+1);
				}
			}
		});
		accept("toggle_god_mode", (params)->{
			toggleShouldFall();
			toggleShouldShowPlayer();
		});
	}
	private boolean shouldFall = true;
	private void toggleShouldFall() {
		shouldFall = !shouldFall;
	}
	Component playerNode = null;
	private void toggleShouldShowPlayer() {
		if(playerNode == null){
			playerNode = (Component)(NodeUtils.getChildByID(getComponent(), getId()+"_body"));
			getComponent().removeChildComponent(playerNode);
		}else {
			getComponent().addChildComponent(playerNode);
			playerNode = null;
		}
	}
	@Override
	protected boolean shouldFall(){
		return shouldFall;
	}

	@Override
	public void attach(Attachable a) {
		if(a instanceof GameCamera){
			super.attach(a);
			gameCamera = (GameCamera)a;
		}
		else{
			if(a.getComponent().getParent() instanceof Component){
				Platform.runLater(()->{
					((Component) a.getComponent().getParent()).removeChildComponent(a.getComponent());
				});
			}
			Platform.runLater(()->{
				((Component)NodeUtils.getChildByID(getComponent(), getId()+"_body")).addChildComponent(a.getComponent());
			});
			a.onAttach(this);
			getAttachables().add(a);
			onAttachActions(a);
		}
	}
	
	@Override
	protected double computeXpReward() {
		return 42069;
	}

	@Override
	public void updateActions(double secondsPassed){
		messenger.send("player_position",get2DPosition(), this);
		messenger.send("position_3D",getPosition(), this);
		Point2D position = get2DPosition();
		int column = map.getCollisionColumnFor(position);
		int row = map.getCollisionRowFor(position);
		ArrayList<Collideable>[][] collisionMap = map.getCollisionMap();
		for (int i = row-1; i < row+1; i++) {
			for (int j = column-1; j < column+1; j++) {
				for (Collideable c : collisionMap[row][column]) {
					if(c instanceof Reachable) {
						if(((Reachable) c).getReachableRadius() > distanceFrom(c)){
							messenger.send("reached_entity", c, this);
						}
					}
				}
			}
		}
	}

	@Override
	public void onCollides(Collideable c) {
	}

	@Override
	public Component buildComponent() {
		
		Component returnVal = new Component(getId());
		Component playerNode = new Component(getId()+"_body");

		double meter = GameLogic.getMeterLength();

		//body boxes
		Box box1 = new Box(0.4*meter,0.4*meter,0.4*meter);
		Box box3 = new Box(0.7*meter, 0.2*meter, 0.2*meter);
		box1.setTranslateY(-box1.getHeight()/2);
		box3.setTranslateY(-box1.getHeight()+box3.getHeight()/2);

		//color for the skin of the player
		PhongMaterial materialHead = new PhongMaterial();
		materialHead.setDiffuseColor(Color.PINK);

		//box for the player head
		Box boxHead = new Box(0.6*meter,0.6*meter,0.6*meter);
		boxHead.setTranslateY(-(boxHead.getHeight()/2+0.4*meter));
		boxHead.setMaterial(materialHead);

		//box for the player's nose
		Box boxNose = new Box(0.1*meter,0.1*meter,0.1*meter);
		boxNose.setTranslateY(-(0.5*meter+boxNose.getHeight()/2));
		boxNose.setTranslateZ(boxHead.getDepth()/2+boxNose.getDepth()/2);
		boxNose.setMaterial(materialHead);

		//boxes for the hat
		Box boxHat = new Box(0.8*meter, 0.05*meter, 0.8*meter);
		Box boxHat2 = new Box(0.5*meter, 0.2*meter, 0.6*meter);
		boxHat.setTranslateY(-(boxHead.getHeight()+box1.getHeight()+boxHat.getHeight()/2));
		boxHat2.setTranslateY(-(boxHead.getHeight()+box1.getHeight()+boxHat.getHeight()+boxHat2.getHeight()/2));

		//constructing the player's head
		Component playerHead = new Component(getId()+"_head");
		playerHead.getChildren().addAll(boxHead, boxNose, boxHat, boxHat2);
		
		//material for the hat
		PhongMaterial hatMaterial = new PhongMaterial();
		hatMaterial.setDiffuseColor(Color.BLACK);
		boxHat.setMaterial(hatMaterial);
		boxHat2.setMaterial(hatMaterial);

		//adding children to the returnValue
		playerNode.getChildren().addAll(box1, box3, playerHead);
		returnVal.addChildComponent(playerNode);
		
		return returnVal;
		/*
		Component realReturnVal = new Component(getId()); 
		Component returnVal = new Component(getId()+"_body");
		realReturnVal.addChildComponent(returnVal);
		double meter = GameLogic.getMeterLength();
		Box body = new Box(0.5*meter,0.5*meter,meter);
		body.setTranslateY(-(0.4*meter));
		body.setMaterial(new PhongMaterial(Color.PINK));
		double width = 0.3*meter;
		double depth = meter;
		double[] boxSize = {0.125*meter, 0.125*meter, 0.125*meter};
		LegComponent[] legs = new LegComponent[4];
		PhongMaterial material = new PhongMaterial(Color.PINK);
		legs[0] = new LegComponent("leg0",0,0, 0.1*meter, boxSize, material);
		legs[0].setTranslateX(-width/2);
		legs[0].setTranslateY(-legs[0].getLeg().getHeight()/2);
		legs[0].setTranslateZ(depth/2);

		legs[1] = new LegComponent("leg1",1,0, 0.1*meter, boxSize, material);
		legs[1].setTranslateX(width/2);
		legs[1].setTranslateY(-legs[1].getLeg().getHeight()/2);
		legs[1].setTranslateZ(depth/2);

		legs[2] = new LegComponent("leg2",1,0, 0.1*meter, boxSize, material);
		legs[2].setTranslateX(-width/2);
		legs[2].setTranslateZ(-depth/2);
		legs[2].setTranslateY(-legs[2].getLeg().getHeight()/2);

		legs[3] = new LegComponent("leg3",0,0, 0.1*meter, boxSize, material);
		legs[3].setTranslateX(width/2);
		legs[3].setTranslateZ(-depth/2);
		legs[3].setTranslateY(-legs[3].getLeg().getHeight()/2);
		
		Box head = new Box(0.4*meter, 0.4*meter, 0.4*meter);
		head.setMaterial(new PhongMaterial(Color.PINK));
		head.setTranslateY(-0.6*meter);
		head.setTranslateZ(0.7*meter);
		
		returnVal.getChildren().addAll(legs);
		returnVal.getChildren().addAll(body, head);
		returnVal.setCursor(Cursor.HAND);
		returnVal.setOnMouseClicked((e) -> {
			TitledPane root;
			try {
				root = FXMLLoader.load(getClass().getResource("/fxml/entity_pane.fxml"));
				//root.setTranslateZ(e.getZ());
				((Group)returnVal.getScene().getRoot()).getChildren().add(root);
				
				root.setTranslateX(e.getSceneX());
				root.setTranslateY(e.getSceneY());
				Label l = (Label)NodeUtils.getChildByID(root, "hpLabel");
				l.setText(String.valueOf(10));
				
				root.setOnMouseClicked((e2)->{
					((Group)returnVal.getScene().getRoot()).getChildren().remove(root);
				});
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		});
		return realReturnVal;*/
	}


	@Override
	protected double computeMovementSpeed() {
		double speed = 15*GameLogic.getMeterLength();
		if(isRunning())
			return speed*1.5;
		return speed;
	}

	@Override
	public void onKeyPressed(KeyEvent ke) {
		keyAction(ke, true);
	}

	@Override
	public void onKeyReleased(KeyEvent ke) {
		keyAction(ke, false);
	}

	@Override
	public void keyAction(KeyEvent ke, boolean keyDown) {
		// TODO Auto-generated method stub
		KeyCode code = ke.getCode();
		switch(code){
			case W:
				this.setUp(keyDown);
				break;
			case A:
				this.setLeft(keyDown);
				break;
			case S:
				this.setDown(keyDown);
				break;
			case D:
				this.setRight(keyDown);
				break;
			case SHIFT:
				break;
			case SPACE:
				break;
			case F:
		}
		if(isDoingAction() && (ke.equals(KeyCode.W) || ke.equals(KeyCode.A) || ke.equals(KeyCode.S) || ke.equals(KeyCode.D))){
			cancelMainAction();
		}
		if(code.equals(KeyCode.SHIFT))
			this.setIsRunning(keyDown);
		else if(code.equals(KeyCode.SPACE))
			this.jump();
		else if(code.equals(KeyCode.F))
			this.dropWeapon();
		ke.consume();
	}

	@Override
	public void additionalComponentUpdates() {
		/*if(!this.movement.equals(Point3D.ZERO)){
			for (int i = 0; i < 4; i++) {
				LegComponent lc = (LegComponent)getComponent().lookup("#leg"+i);
				lc.update();
			}
		}*/
	}

	@Override
	protected Point3D getJumpVector() {
		return jumpVector;
	}

	@Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	public void updateComponent(){
		Component playerBody = getComponent().getSubComponent(getId()+"_body");
		getComponent().setPosition(getPosition());
		if(!isDoingAction()) {
			if(playerBody != null) {
				playerBody.setRotationAxis(Rotate.Y_AXIS);
				playerBody.setRotate(computeComponentRotationAngle(rotationAngle));				
			}
		}
		additionalComponentUpdates();
	}
	@Override
	protected void jump(){
		//overriden from LivingEntity
		addForceToGravity(getJumpVector());
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}

	@Override
	protected String getMouseToolTipText() {
		return "You";
	}

	@Override
	public void onClick(MouseEvent me) {
		
	}

	@Override
	protected Parent buildOnClickedPane() {
		return null;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		return null;
	}
	@Override
	protected void takeDamage(double amount, MessageReceiver attacker) {
		super.takeDamage(amount, attacker);
		App.getUserInterface().getGameScreen().showNewHP(hp, maxHp);
	}
	/**
	 * for the player to look in the direction of the mouse
	 */
	private double mouseX = 0;
	private double mouseY = 0;
	@Override
	protected double computeAngleFromMovement(Point3D movement) {
		double ang = Math.toDegrees(Math.atan2(mouseY, mouseX));
		return ang;
	}
	@Override
	protected void onDeath(){
		detach(gameCamera);
		super.onDeath();
	}

	public Attachable getGameCamera() {
		return gameCamera;
	}

}
