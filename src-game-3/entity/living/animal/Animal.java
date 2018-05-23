package entity.living.animal;

import java.io.IOException;

import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import entity.drops.HealthBoost;
import entity.living.LivingEntity;
import game.GameLogic;
import game.Map;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import util.NodeUtils;
import util.PositionGenerator;
import visual.Component;

public abstract class Animal extends LivingEntity{
	
	public Animal(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		submissionFactor = computeSubmissionFactor();
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
            	startMovingTo(PositionGenerator.generate2DPositionInRadius(get2DPosition(), 1000));
        }
	}
	private double submissionFactor;
	public double getSubmissionFactor() {
		return submissionFactor;
	}
	protected abstract double computeSubmissionFactor(); 

	@Override
	public double computeCollidingWeight() {
		return 1;
	}

	@Override
	protected double computeMovementSpeed() {
		return 5*GameLogic.getMeterLength();
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}
	
	@Override
	protected Parent buildOnClickedPane() {
		TitledPane root;
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/entity_pane.fxml"));
			root.setText(getClass().getSimpleName());
			Label hpLabel = (Label)NodeUtils.getChildByID(root, "hpLabel");
			hpLabel.setText(String.valueOf(10));
			return root;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		return onClickedPane;
	}

}
