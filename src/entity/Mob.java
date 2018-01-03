package entity;

import java.util.concurrent.ThreadLocalRandom;

import dao.FloorMatrix;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.TriangleMesh;

public abstract class Mob extends GameElement{
	private Group mobNode;
	private TriangleMesh mesh;
	private Point3D position;
	private Point3D cible;
	private Point3D normeDeplacement;
	private Point3D normeSaut;
	private Point3D gravite;
	private boolean saute;
	private double degresAngleDirection;
	
	
	
	public Mob(Point3D position, double degresAngleDirection){
		gravite = new Point3D(0,4,0);
		normeSaut = new Point3D(0,0,0);
		this.position = position;
		this.normeDeplacement = new Point3D(0,0,0);
		this.degresAngleDirection = degresAngleDirection;
		this.mobNode = new Group();
		this.mobNode.getChildren().addAll(new Box(100, 100, 100));
	}
	public Mob(Point3D position, Point3D cible){
		gravite = new Point3D(0,4,0);
		normeSaut = new Point3D(0,0,0);
		this.position = position;
		ciblerDestination(cible);
		this.mobNode = new Group();
		Box boite1 = new Box(25,25,25);
		this.mobNode.getChildren().addAll(boite1);
		boite1.setTranslateY(-boite1.getHeight()/2);
	}
	/**
	 * Cette fonction retourne l'objet Group contenant tous les éléments visuels pour dessiner le mob à l'écran
	 * @return la Node contenant tous les éléments pour dessiner la node
	 */
	public Group getMobNode() {
		return mobNode;
	}
	
	/**
	 * Cette fonction configure l'objet {@link TriagnleMesh}
	 * @param mesh l'objet {@link TriangleMesh} à utiliser pour dessiner l'objet à l'écran
	 */
	public void setMesh(TriangleMesh mesh){
		this.mesh = mesh;
	}
	/**
	 * Cette fonction retourne l'objet {@link TriangleMesh} de l'objet en question
	 * @return l'objet TriangleMesh nécessaire à dessiner le mob à l'écran
	 */
	public TriangleMesh getMesh(){
		return mesh;
	}
	/**
	 * Cette fonction définit la cible vers laquelle le mob va marcher
	 * @param cible la position de la nouvelle destination du mob
	 */
	public void ciblerDestination(Point3D cible){
		this.cible = cible;
		Point3D norme = cible.subtract(position).normalize();
		normeDeplacement = norme;
		rafraichirOrientation();
	}
	/**
	 * Cette fonction rafraichit l'angle de rotation du mob
	 */
	public void rafraichirOrientation(){
		//le nouvel angle de direction
		Point2D coord2DDirection = new Point2D(normeDeplacement.getX(), normeDeplacement.getZ());
		degresAngleDirection = new Point2D(1, 0).angle(coord2DDirection);
	}
	/**
	 * cette fonction rafraichit l'état du mob.
	 */
	public void refresh() {
		animer();
	}
	/**
	 * Cette fonction anime le mob. 
	 */
	public void animer(){
		deplacer();
		deplacerObjet3D();
	}
	public void sauter(){
		saute = true;
		normeSaut = new Point3D(0,-50,0);
	}
	/**
	 * cette fonction déplace le mob
	 */
	private void deplacer(){
		if(saute) {
			position = position.add(normeSaut);
			double floorHeight = FloorMatrix.getInstance().getHeighAt(position.getX(), position.getZ());
			if(-(position.getY()-floorHeight) <= -(gravite.getY()+1) && -(normeSaut.getY()) <= 0) {
				position = new Point3D(position.getX(), 0, position.getZ());
				normeSaut = new Point3D(0,0,0);
				saute = false;
			}
			else {
				normeSaut = normeSaut.add(gravite);
			}
		}
		if(position.distance(cible) < normeDeplacement.distance(Point3D.ZERO)){
			position = cible;
			normeDeplacement = Point3D.ZERO;
			//debug
			int min = -500;
			int max = 500;
			double cibleX = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			double cibleZ = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			Point3D cib = new Point3D(cibleX, 0, cibleZ);
			ciblerDestination(cib);
		}
		else {
			position = position.add(normeDeplacement);
			//System.out.println(position.getX()+" "+position.getY()+" "+position.getZ());
		}
	}
	private void deplacerObjet3D() {
		mobNode.setTranslateX(position.getX());
		mobNode.setTranslateY(position.getY());
		mobNode.setTranslateZ(position.getZ());
	}
}
