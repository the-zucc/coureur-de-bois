package entity.statics.village;

import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import visual.Component;

public class Cafe extends House {
    public Cafe(Point3D position, Map map, Messenger messenger) {
        super(position, map, messenger);
    }
    public Component buildComponent(){
        Component returnVal = super.buildComponent();
        Group signContainer = new Group();
        Image image = new Image("assets/coffee-beans.png");
        Label text = new Label("",new ImageView(image));
        Bounds textBounds = text.getBoundsInLocal();
        double textWidth = textBounds.getWidth();
        double textHeight = textBounds.getHeight();
        Box sign = new Box(textWidth,textHeight, 0.1* GameLogic.getMeterLength());

        signContainer.getChildren().addAll(sign, text);
        signContainer.setTranslateY(-10);

        returnVal.getChildren().add(signContainer);
        return returnVal;
    }
}
