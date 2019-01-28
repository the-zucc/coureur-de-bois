package entity.living.human;

import java.io.IOException;

import characteristic.Messenger;
import characteristic.interactive.Hoverable;
import characteristic.positionnable.Collideable;
import entity.drops.MaxHealthBoost;
import entity.living.LivingEntity;
import entity.wearable.LongSword;
import entity.wearable.StandardSword;
import entity.wearable.WeaponEntity;
import game.GameLogic;
import game.Map;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import util.NodeUtils;
import village.Village;
import visual.Component;

public class Villager extends Human implements Hoverable{
	
	private Village village;
	public void setVillage(Village v){
	    this.village = v;
    }
    public Village getVillage(){
	    return this.village;
	}

	public Villager(Point3D position, Map map, Messenger messenger, WeaponEntity e) {
		super(position, map, messenger, (int)(Math.random()*10)+1);
		accept("yall_jump", (params)->{
			jump();
		});
		accept("wield_weapon", (params)->{
        	if(params[0] == this) {
        		wieldWeapon((WeaponEntity)params[1]);
        	}
        });
		accept("player_position", (params)->{
			if(((Point2D)params[0]).distance(get2DPosition()) < 10*GameLogic.getMeterLength()) {
				addUpdate(()->{
					boolean hasWeapon = this.wieldedWeapon != null;
					double maxDist;
					if(hasWeapon && wieldedWeapon instanceof StandardSword) {
						maxDist = hasWeapon ? ((StandardSword)wieldedWeapon).getSwordLength()*1.5 : GameLogic.getMeterLength()*1.5;
					}
					else {
						maxDist = GameLogic.getMeterLength();
					}
					boolean checkDistance = Villager.this.getPosition().distance(((LivingEntity)params[1]).getPosition()) < maxDist;
					if(checkDistance) {
						attack((LivingEntity)params[1],10);									
					}
				}, ()->{
					return ((Player)params[1]).get2DPosition().distance(get2DPosition()) > 10*GameLogic.getMeterLength() && ((Player)params[1]).getHp() <= 0;
				}, ()->{
					this.startDoingAction();
				});
			}
		});
		/*
		if(Math.random()>0.65) {*/
		WeaponEntity entity = Math.random() > 0.8 ? new LongSword(getPosition(), map, messenger) : new StandardSword(getPosition(), map, messenger);
		map.addEntity(entity);
		wieldWeapon(entity);
		//}
	}
    public Villager(Point3D position, Map map, Messenger messenger, Village v) {
        super(position, map, messenger, (int)(Math.random()*10)+1);
        village = v;
        accept("wield_weapon", (params)->{
        	if(params[0] == this) {
        		wieldWeapon((WeaponEntity)params[1]);
        	}
        });
        accept("player_position", (params)->{
			if(((Point2D)params[0]).distance(get2DPosition()) < 10*GameLogic.getMeterLength()) {
				addUpdate(()->{
					startMovingTo(((Player)params[1]).get2DPosition());
					boolean hasWeapon = this.wieldedWeapon != null;
					double maxDist;
					if(hasWeapon && wieldedWeapon instanceof StandardSword) {
						maxDist = hasWeapon ? ((StandardSword)wieldedWeapon).getSwordLength()*1.5 : GameLogic.getMeterLength()*1.5;
					}
					else {
						maxDist = GameLogic.getMeterLength();
					}
					boolean checkDistance = Villager.this.getPosition().distance(((LivingEntity)params[1]).getPosition()) < maxDist;
					if(checkDistance) {
						attack((LivingEntity)params[1],10);									
					}
				}, ()->{
					return ((Player)params[1]).get2DPosition().distance(get2DPosition()) > 10*GameLogic.getMeterLength() && ((Player)params[1]).getHp() <= 0;
				}, ()->{
					this.startDoingAction();
				});
			}
		});
        WeaponEntity entity = Math.random() > 0.8 ? new LongSword(getPosition(), map, messenger) : new StandardSword(getPosition(), map, messenger);
		map.addEntity(entity);
		wieldWeapon(entity);
    }

	@Override
	public void additionalComponentUpdates(){
		
    }

	@Override
	protected double computeXpReward() {

		return 0;
	}

	@Override
	public void updateActions(double secondsPassed) {
	    double actionChoiceTreshold = 0.015;
        double a = Math.random()*100;
        if (a < actionChoiceTreshold) {
            setUp(true);
        } else if (a < 2 * actionChoiceTreshold) {
            setUp(false);
        } else if (a < 3 * actionChoiceTreshold) {
            setDown(true);
        } else if (a < 4 * actionChoiceTreshold) {
            setDown(false);
        } else if (a < 5 * actionChoiceTreshold) {
            setLeft(true);
        } else if (a < 6 * actionChoiceTreshold) {
            setLeft(false);
        } else if (a < 7 * actionChoiceTreshold) {
            setRight(true);
        } else if (a < 8 * actionChoiceTreshold) {
            setRight(false);
        } else if (a < 9 * actionChoiceTreshold) {
			startMovingTo(village.get2DPosition());
		} else {
            double mapWidth = Map.getMainMap().getMapWidth();
            if(getPosition().getX() > mapWidth/2 || getPosition().getX() < -mapWidth/2)
                startMovingTo(village.get2DPosition());
        }
	}

	@Override
	protected double computeMovementSpeed() {
		return 6*GameLogic.getMeterLength();
	}
	
	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
        Box box1 = new Box(0.4*meter,0.4*meter,0.4*meter);
        Box box2 = new Box(0.6*meter, 0.6*meter,0.6*meter);
        Box box3 = new Box(0.6*meter,0.2*meter,0.2*meter);
        Box boxNose = new Box(0.1*meter,0.12*meter,0.1*meter);
        PhongMaterial materialHead = new PhongMaterial();
        materialHead.setDiffuseColor(Color.PINK);
        box2.setMaterial(materialHead);
        boxNose.setMaterial(materialHead);

        box1.setTranslateY(-0.25*meter);
        box2.setTranslateY(-0.7*meter);
        box3.setTranslateY(-0.2*meter +.015*meter);

        boxNose.setTranslateY(-0.7*meter);
        boxNose.setTranslateZ(0.25*meter+0.1*meter);

        //hair
        PhongMaterial materialHair = new PhongMaterial(Color.BLACK);
        Box boxHair = new Box(0.695*meter, 0.1*meter, 0.695*meter);
        //Box boxHair2 = new Box(5, )
        boxHair.setMaterial(materialHair);
        boxHair.setTranslateY(-meter);

        returnVal.getChildren().addAll(box1, box2, box3, boxNose, boxHair);

		returnVal.setOnMouseEntered(e -> {
			this.onHover(e);
		});
		returnVal.setOnMouseExited(e -> {
			this.onUnHover(e);
		});
		returnVal.setCursor(Cursor.HAND);
		return returnVal;
	}
	
	@Override
	public void onCollides(Collideable c) {
		
	}
	
	public double getDistanceFromVillage(){
		return get2DPosition().distance(village.get2DPosition());
	}
	@Override
	public double computeCollidingWeight() {
		return 1;
	}
	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.HAND;
	}
	@Override
	protected String getMouseToolTipText() {
		return "Villager";
	}
	@Override
	public void onClick(MouseEvent me) {
		
	}
	@Override
	protected Parent buildOnClickedPane() {
		
		try {
			Parent returnVal = FXMLLoader.load(getClass().getResource("/fxml/entity_pane.fxml"));
			((Label)(NodeUtils.getChildByID(returnVal, "hpLabel"))).setText(String.valueOf(getHp()));
			return returnVal;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		return onClickedPane;
	}
	@Override
	protected void onDeath() {
		super.onDeath();
		double meter = GameLogic.getMeterLength();
		messenger.send("drop", Math.random() > 0.85 ? new LongSword(getPosition(), map, messenger) : new MaxHealthBoost(getPosition(), map, messenger));
	}
}
