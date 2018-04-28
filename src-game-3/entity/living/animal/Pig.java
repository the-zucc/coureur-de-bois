package entity.living.animal;

import java.io.IOException;
import java.io.InputStream;

import app.App;
import characteristic.Messenger;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import collision.SphericalCollisionBox;
import entity.living.LivingEntity;
import game.GameLogic;
import game.Map;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import ui.UserInterface;
import ui.gamescene.GameScreen;
import util.NodeUtils;
import util.PositionGenerator;
import visual.Component;
import visual.LegComponent;

public class Pig extends LivingEntity {
	

	public Pig(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		accept("yall_jump", (params)->{
			jump();
		});
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
		if(!this.movement.equals(Point3D.ZERO)){
			for (int i = 0; i < 4; i++) {
				LegComponent lc = (LegComponent)getComponent().lookup("#leg"+i);
				lc.update();
			}
		}
	}

	@Override
	protected double computeMovementSpeed() {
		return 5*GameLogic.getMeterLength();
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
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
		return returnVal;
	}

	@Override
	public CollisionBox buildCollisionBox() {
		return new SphericalCollisionBox(0.5*GameLogic.getMeterLength(),this, new Point3D(0,0,0), map);
	}

	@Override
	public void onCollides(Collideable c) {
		
	}

	@Override
	public void onHover(MouseEvent me) {
		
	}

	@Override
	public void onUnHover(MouseEvent me) {
		
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.HAND;
	}

	@Override
	protected String getMouseToolTipText() {
		return "Pig";
	}
}
