package entity.statics.tree;

import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import util.MeshFactory;
import visual.Component;

public class PineTree extends TreeNormal {

    public PineTree(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
    }

    @Override
    public Component buildComponent() {

        return recurseBuildPyramids(getId(), (float)(4*GameLogic.getMeterLength()), (float)(2.4*GameLogic.getMeterLength()), 2);
    }

    private Component recurseBuildPyramids(String id, float baseSectionHeight, float baseSectionRadius, int sectionCount){
        Component returnVal = new Component(getId());

        returnVal.getChildren().add(pyramidCreate(-baseSectionHeight/2,baseSectionHeight+(float)(0.3*GameLogic.getMeterLength()) ,baseSectionRadius,null, sectionCount));

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
        mv1.setMaterial(new PhongMaterial(Color.DARKGREEN));

        returnVal.getChildren().add(mv1);

        if(remainingSectionCount > 0){
            return pyramidCreate((float)returnVal.getBoundsInLocal().getMinY()+sectionHeight/2, sectionHeight*0.75f, sectionRadius*0.65f, returnVal, remainingSectionCount-1);
        }
        else
            return returnVal;
    }
}
