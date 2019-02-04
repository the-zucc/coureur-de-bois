package entity.living.animal;

import characteristic.Messenger;
import entity.living.LivingEntity;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import util.PositionGenerator;

public abstract class Animal extends LivingEntity{
	
	public Animal(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		submissionFactor = computeSubmissionFactor();
		
	}
	@Override
	public void updateActions(float secondsPassed) {
		float actionChoiceTreshold = 0.015f;
        float a = (float)Math.random()*100;
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
            double mapWidth = Map.getMainMap().getMapWidth();
            if(getPosition().getX() > mapWidth/2 || getPosition().getX() < -mapWidth/2)
            	startMovingTo(PositionGenerator.generate2DPositionInRadius(get2DPosition(), 1000));
        }
        if(submissionFactor < 0.05) {
        	messenger.send("position_3D", getPosition(), this);
        }
	}
	private float submissionFactor;
	public float getSubmissionFactor() {
		return submissionFactor;
	}
	protected abstract float computeSubmissionFactor();

	@Override
	public float computeCollidingWeight() {
		return 1;
	}

	@Override
	protected float computeMovementSpeed() {
		return 5*GameLogic.getMeterLength();
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}
	
	@Override
	protected Parent buildOnClickedPane() {
		return null;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		return onClickedPane;
	}

}
