package entity;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;

public class Player{
	
	private Group playerNode;
	private Point3D position;
	
	public Player(int id, String nom, Point3D positionDepart) {
		playerNode = new Group();
		playerNode.setId(nom);
		position = positionDepart;
		Box boitePlayer = new Box(30, 30, 30);
		boitePlayer.setTranslateY(-100);
		playerNode.getChildren().add(boitePlayer);
	}
	
	public void refresh(Point3D normeDeplacement) {
		position = position.add(normeDeplacement);
		refreshVisuel();
	}
	public Group getPlayerNode() {
		return playerNode;
	}
	public void refreshVisuel() {
		playerNode.setTranslateX(position.getX());
		playerNode.setTranslateY(position.getY());
		playerNode.setTranslateZ(position.getZ());
	}
}
