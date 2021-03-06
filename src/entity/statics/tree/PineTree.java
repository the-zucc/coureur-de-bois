package entity.statics.tree;

import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import util.MeshFactory;
import visual.Component;

import java.util.concurrent.ThreadLocalRandom;

public class PineTree extends Tree {
    public PineTree(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
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
        treeScale = ThreadLocalRandom.current().nextDouble(1.5)+1;

        returnVal.setScaleX(treeScale);
        returnVal.setScaleY(treeScale);
        returnVal.setScaleZ(treeScale);
        
        return returnVal;
    }

    private Component recurseBuildPyramids(String id, float baseSectionHeight, float baseSectionRadius, int sectionCount){
        Component returnVal = new Component(getId());
        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image("res/leaves-cartoon.png"));
        returnVal.getChildren().add(pyramidCreate(-baseSectionHeight/3,baseSectionHeight+(float)(0.3*GameLogic.getMeterLength()) ,baseSectionRadius,null, sectionCount, material));

        return returnVal;
    }

    private Group pyramidCreate(float translateY, float sectionHeight, float sectionRadius, Group group, int remainingSectionCount, PhongMaterial material){
        Group returnVal;
        if(group == null) {
            returnVal = new Group();
        }
        else
            returnVal = group;
        MeshView mv1 = MeshFactory.buildRegularPyramid(6, sectionHeight, sectionRadius, material);
        mv1.setRotationAxis(Rotate.Y_AXIS);
        mv1.setRotate(Math.random()*Math.PI*2);
        mv1.setTranslateY(translateY);
        mv1.setMaterial(material);

        returnVal.getChildren().add(mv1);

        if(remainingSectionCount > 0){
            return pyramidCreate((float)returnVal.getBoundsInLocal().getMinY()+sectionHeight/2, sectionHeight*0.75f, sectionRadius*0.65f, returnVal, remainingSectionCount-1, material);
        }
        else
            return returnVal;
    }
    @Override
    protected String getMouseToolTipText() {
    	return "Pine tree";
    }
}
