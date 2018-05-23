package entity.drops;

import characteristic.Messenger;
import game.GameLogic;
import game.Map;
import javafx.geometry.Point3D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;
import visual.Component;

public class WoodPiece extends DroppableFloatingEntity{

	public WoodPiece(Point3D position, Map map, Messenger messenger) {
		super(position, map, messenger);
		
	}

	@Override
	public void onClick(MouseEvent me) {
		
	}

	@Override
	protected boolean canBePickedUp() {
		return true;
	}

	@Override
	protected void onPickup(Object... params) {
		messenger.send("item_picked_up", params[1], this);
		messenger.send("remove", this);
	}

	@Override
	protected Cursor getHoveredCursor() {
		return Cursor.DEFAULT;
	}

	@Override
	protected String getMouseToolTipText() {
		return "Pieces of wood";
	}

	@Override
	public Component buildComponent() {
		Component returnVal = new Component(getId());
		double meter = GameLogic.getMeterLength();
		PhongMaterial woodMaterial = new PhongMaterial(Color.BROWN);
		Box pieceOfWood1 = new Box(0.4*meter, meter, 0.4*meter);
		pieceOfWood1.setMaterial(woodMaterial);
		pieceOfWood1.getTransforms().add(new Rotate(45, Rotate.Z_AXIS));
		Box pieceOfWood2 = new Box(0.4*meter, meter, 0.4*meter);
		pieceOfWood2.setMaterial(woodMaterial);
		pieceOfWood2.getTransforms().add(new Rotate(45, Rotate.Z_AXIS));
		pieceOfWood2.setTranslateX(pieceOfWood1.getWidth()*1.01);
		pieceOfWood2.setTranslateZ(pieceOfWood1.getDepth()*1.01);
		returnVal.getChildren().addAll(pieceOfWood1, pieceOfWood2);
		return returnVal;
	}

	@Override
	protected Parent buildOnClickedPane() {
		return null;
	}

	@Override
	protected Node getPaneDismissNode(Parent onClickedPane) {
		return null;
	}

}
