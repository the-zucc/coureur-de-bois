package entity.living.animal;

import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
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
import visual.LegComponent;

public class Fox extends LivingEntity {
	

	public Fox(Point3D position, Map map) {
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
        /*if (a < actionChoiceTreshold) {
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
        }*/
        startMovingTo(Point2D.ZERO);
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
		double meter = GameLogic.getMeterLength()*10;
		Box body = new Box(0.5*meter,0.5*meter,meter);
		body.setTranslateY(-(0.4*meter));
		body.setMaterial(new PhongMaterial(Color.CHOCOLATE));
		double width = 0.3*meter;
		double depth = meter;
		double[] boxSize = {0.125*meter, 0.125*meter, 0.125*meter};
		LegComponent[] legs = new LegComponent[4];
		PhongMaterial material = null;
		legs[0] = new LegComponent("leg0",0,0, 0.1*meter, boxSize, material);
		legs[0].setTranslateX(-width/2);
		legs[0].setTranslateY(-legs[0].getLeg().getHeight()/2);
		legs[0].setTranslateZ(depth/2);

		legs[1] = new LegComponent("leg1",0,0, 0.1*meter, boxSize, material);
		legs[1].setTranslateX(width/2);
		legs[1].setTranslateY(-legs[1].getLeg().getHeight()/2);
		legs[1].setTranslateZ(depth/2);

		legs[2] = new LegComponent("leg2",0,0, 0.1*meter, boxSize, material);
		legs[2].setTranslateX(-width/2);
		legs[2].setTranslateZ(-depth/2);
		legs[2].setTranslateY(-legs[2].getLeg().getHeight()/2);

		legs[3] = new LegComponent("leg3",0,0, 0.1*meter, boxSize, material);
		legs[3].setTranslateX(width/2);
		legs[3].setTranslateZ(-depth/2);
		legs[3].setTranslateY(-legs[3].getLeg().getHeight()/2);
		
		Box head = new Box(0.4*meter, 0.4*meter, 0.4*meter);
		head.setMaterial(new PhongMaterial(Color.BLANCHEDALMOND));
		head.setTranslateY(-0.6*meter);
		head.setTranslateZ(35);
		
		returnVal.getChildren().addAll(legs);
		returnVal.getChildren().addAll(body, head);
		return returnVal;
	}

	@Override
	public CollisionBox buildCollisionBox() {
		return new SphericalCollisionBox(0.5*GameLogic.getMeterLength(),this, new Point3D(0,-GameLogic.getMeterLength()*10,0), map);
	}

	@Override
	public void onCollides(Collideable c) {
		
	}

}
