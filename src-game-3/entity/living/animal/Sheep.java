package entity.living.animal;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.living.LivingEntity;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import util.PositionGenerator;
import visual.Component;

public class Sheep extends LivingEntity {


	public Sheep(Point3D position, Map map) {
		super(position, map);
	}

	@Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	protected double computeXpReward() {
		return 100;
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
			startMovingTo(PositionGenerator.generate2DPositionInRadius(get2DPosition(), 1000));
		} else {
            double mapWidth = Map.getInstance().getMapWidth();
            if(getPosition().getX() > mapWidth/2 || getPosition().getX() < -mapWidth/2)
                startMovingTo(Point2D.ZERO);
        }

	}

	@Override
	public void additionalComponentUpdates() {
		
	}

	@Override
	protected double computeMovementSpeed() {
		return 5*GameLogic.getMeterLength();
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		Box box = new Box(meter,meter,meter*1.2);
		box.setTranslateY(-meter/2);
		
		Box head = new Box(0.4*meter, 0.4*meter, 0.4*meter);
		head.setMaterial(new PhongMaterial(Color.BLACK));
		head.setTranslateY(-0.6*meter);
		head.setTranslateZ(0.7*meter);
		
		returnVal.getChildren().addAll(box, head);
		return returnVal;
	}

	@Override
	public CollisionBox buildCollisionBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCollides(Collideable c) {
		
	}

}
