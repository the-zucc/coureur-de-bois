package entity.living.human;

import characteristic.interactive.Hoverable;
import characteristic.positionnable.Collideable;
import collision.CollisionBox;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
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

	public Villager(Point3D position) {
		super(position, (int)(Math.random()*10)+1);
	}
    public Villager(Point3D position, Village v) {
        super(position, (int)(Math.random()*10)+1);
        village = v;
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
            double mapWidth = Map.getInstance().getMapWidth();
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

        Box box1 = new Box(20,20,20);
        Box box2 = new Box(30,30,30);
        Box box3 = new Box(30, 10, 10);
        Box boxNose = new Box(5,7,5);
        PhongMaterial materialHead = new PhongMaterial();
        materialHead.setDiffuseColor(Color.PINK);
        box2.setMaterial(materialHead);
        boxNose.setMaterial(materialHead);

        box1.setTranslateY(-12.5);
        box2.setTranslateY(-35);
        box3.setTranslateY(-10.75);

        boxNose.setTranslateY(-35);
        boxNose.setTranslateZ(17.5);

        //hair
        PhongMaterial materialHair = new PhongMaterial(Color.ORANGERED);
        Box boxHair = new Box(34, 5, 34);
        //Box boxHair2 = new Box(5, )
        boxHair.setMaterial(materialHair);
        boxHair.setTranslateY(-50);

        returnVal.getChildren().addAll(box1, box2, box3, boxNose, boxHair);

		returnVal.setOnMouseEntered(e -> {
			this.onHover(e);
		});
		returnVal.setOnMouseExited(e -> {
			this.onUnHover(e);
		});
		return returnVal;
	}

	@Override
	public CollisionBox buildCollisionBox() {
		return null;
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
	
	public double getDistanceFromVillage(){
		return get2DPosition().distance(village.get2DPosition());
	}
}
