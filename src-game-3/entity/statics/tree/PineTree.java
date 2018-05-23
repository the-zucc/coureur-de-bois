package entity.statics.tree;

import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import util.MeshFactory;
import visual.Component;

public class PineTree extends Tree {
	private PhongMaterial material;
	private double scale;
    public PineTree(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
        if(Math.random() > 0.7) {
        	material = new PhongMaterial(Color.DARKGREEN);        	
        }else {
        	material = new PhongMaterial(Color.WHITE);
        }
    }

    @Override
    public Component buildComponent() {
    	PhongMaterial trunkMaterial = new PhongMaterial(Color.SADDLEBROWN);
    	Component returnVal = recurseBuildPyramids(getId(), (float)(4*GameLogic.getMeterLength()), (float)(2.4*GameLogic.getMeterLength()), 2);
    	double meter = GameLogic.getMeterLength();
    	Box trunk = new Box(0.75*meter, 2*meter, 0.75*meter);
    	trunk.setTranslateY(-trunk.getHeight()/2);
    	trunk.setMaterial(trunkMaterial);
    	returnVal.getChildren().add(trunk);
        return returnVal;
    }

    private Component recurseBuildPyramids(String id, float baseSectionHeight, float baseSectionRadius, int sectionCount){
        Component returnVal = new Component(getId());

        returnVal.getChildren().add(pyramidCreate(-baseSectionHeight/3,baseSectionHeight+(float)(0.3*GameLogic.getMeterLength()) ,baseSectionRadius,null, sectionCount));

        return returnVal;
    }

    private Group pyramidCreate(float translateY, float sectionHeight, float sectionRadius, Group group, int remainingSectionCount){
        Group returnVal;
        if(group == null) {
            returnVal = new Group();
        }
        else
            returnVal = group;

        MeshView mv1 = MeshFactory.buildRegularPyramid(6, sectionHeight, sectionRadius);
        mv1.setRotationAxis(Rotate.Y_AXIS);
        mv1.setRotate(Math.random()*Math.PI*2);
        mv1.setTranslateY(translateY);
        mv1.setMaterial(material);
        

        returnVal.getChildren().add(mv1);

        if(remainingSectionCount > 0){
            return pyramidCreate((float)returnVal.getBoundsInLocal().getMinY()+sectionHeight/2, sectionHeight*0.75f, sectionRadius*0.65f, returnVal, remainingSectionCount-1);
        }
        else
            return returnVal;
    }
    @Override
    protected String getMouseToolTipText() {
    	return "Pine tree";
    }
}
