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
import javafx.scene.Node;
import javafx.scene.Parent;
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

public class Fox extends LivingEntity {
	

	public Fox(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		accept("yall_jump", (params)->{
			jump();
		});
		accept("player_position",(params)->{
			Point2D playerPos = (Point2D)params[0];
			if(playerPos.distance(get2DPosition()) < 10 * GameLogic.getMeterLength()) {
				startMovingTo(playerPos);					
			}
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
		PhongMaterial material = new PhongMaterial(Color.ORANGE);
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		Box body = new Box(0.3*meter,0.3*meter,0.8*meter);
		body.setTranslateY(-(0.4*meter));
		body.setMaterial(material);
		double width = 0.20*meter;
		double depth = 0.7*meter;
		double[] boxSize = {0.125*meter, 0.125*meter, 0.125*meter};
		LegComponent[] legs = new LegComponent[4];
		
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
		
		Box head = new Box(0.3*meter, 0.3*meter, 0.3*meter);
		head.setMaterial(material);
		head.setTranslateY(-0.6*meter);
		head.setTranslateZ(0.5*meter);
		
		
		Box headBox2 = new Box(0.15*meter, 0.15*meter, 0.15*meter);
		headBox2.setTranslateZ(head.getBoundsInParent().getMaxZ()+headBox2.getDepth()/2);
		headBox2.setTranslateY(head.getBoundsInParent().getMaxY()-headBox2.getHeight()/2);
		headBox2.setMaterial(material);
		
		//EARS
		double earWidth = head.getWidth()*0.8;
		Box ear1 = new Box(0.1*meter, 0.1*meter,0.05*meter);
		Box ear12 = new Box(0.07*meter, 0.07*meter,0.04*meter);
		Box ear2 = new Box(0.1*meter, 0.1*meter,0.05*meter);
		Box ear22 = new Box(0.07*meter, 0.07*meter,0.04*meter);
		
		PhongMaterial earMaterial = new PhongMaterial(Color.DARKGOLDENROD);
		ear1.setMaterial(earMaterial);
		ear1.setTranslateZ(head.getBoundsInParent().getMinZ());
		ear1.setTranslateY(head.getBoundsInParent().getMinY()+ear1.getHeight()/2);
		ear1.setTranslateX(earWidth/2);
		ear12.setTranslateX(ear1.getTranslateX());
		ear12.setTranslateY(ear1.getTranslateY());
		ear12.setTranslateZ(ear1.getTranslateZ()+0.25*ear1.getDepth());
		
		ear2.setMaterial(earMaterial);
		ear2.setTranslateZ(head.getBoundsInParent().getMinZ());
		ear2.setTranslateY(head.getBoundsInParent().getMinY()+ear2.getHeight()/2);
		ear2.setTranslateX(-earWidth/2);
		ear22.setTranslateX(ear2.getTranslateX());
		ear22.setTranslateY(ear2.getTranslateY());
		ear22.setTranslateZ(ear2.getTranslateZ()+0.25*ear2.getDepth());
		
		returnVal.getChildren().addAll(legs);
		returnVal.getChildren().addAll(body, head, headBox2, ear1, ear12, ear2, ear22);
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
		return "Fox";
	}

	@Override
	public void onClick(MouseEvent e) {
		
	}

	@Override
	protected Parent buildOnClickedPane() {
		TitledPane root;
		try {
			root = FXMLLoader.load(getClass().getResource("/fxml/entity_pane.fxml"));
			Label l = (Label)NodeUtils.getChildByID(root, "hpLabel");
			l.setText(String.valueOf(10));
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
