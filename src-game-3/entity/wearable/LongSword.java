package entity.wearable;

import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import visual.Component;

public class LongSword extends StandardSword{

    public LongSword(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
    }
    @Override
    public Component buildComponent() {
        PhongMaterial trimPieceMaterial = new PhongMaterial(Color.BROWN);
        PhongMaterial handleMaterial = new PhongMaterial(Color.BLACK);
        Component returnVal = new Component(getId());
        double meter = GameLogic.getMeterLength();
        Box sword = new Box(0.1*meter, meter*1.2,0.3*meter);
        Box handle = new Box(0.101*meter, 0.4*meter, 0.1*meter);
        handle.setTranslateY(-handle.getHeight()/2);
        sword.setTranslateY(-handle.getHeight()-sword.getHeight()/2);
        Box trimPiece = new Box(0.102*meter, 0.1*meter, 0.45*meter);
        trimPiece.setTranslateY(-handle.getHeight());
        trimPiece.setMaterial(trimPieceMaterial);
        handle.setMaterial(handleMaterial);
        Box swordTip = new Box(0.1*meter, 0.1*meter, sword.getDepth()/2);
        swordTip.setTranslateY(sword.getTranslateY()-sword.getHeight()/2-swordTip.getHeight()/2);
        returnVal.getChildren().addAll(handle, sword, trimPiece, swordTip);
        return returnVal;
    }
    @Override
    protected String getMouseToolTipText() {
        return "Long sword";
    }
}
