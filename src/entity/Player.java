package entity;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;

public class Player extends GameElement{
	private Group playerNode;
	private Point3D normeDeplacement;
	private Direction dir;
	private Point3D position;
	private Point3D zAug;
	private Point3D xAug;
	private Point3D zDim;
	private Point3D xDim;
	public Player(int id, String nom, Point3D positionDepart) {
		playerNode = new Group();
		playerNode.setId(nom);
		position = positionDepart;
		Box boitePlayer = new Box(30, 30, 30);
		boitePlayer.setTranslateY(-100);
		playerNode.getChildren().add(boitePlayer);
		double norme = 2;
		zAug = new Point3D(0, 0, norme);
		xAug = new Point3D(norme, 0, 0);
		zDim = new Point3D(0, 0, -norme);
		xDim = new Point3D(-norme, 0, 0);
	}
	
	public void refresh() {
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
	public void setDirection(Direction dir) {
		this.dir = dir;
	}
}
