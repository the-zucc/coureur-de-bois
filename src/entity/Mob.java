package entity;

import java.util.concurrent.ThreadLocalRandom;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.shape.Box;
import javafx.scene.shape.TriangleMesh;

public abstract class Mob {
	private Group mobNode;
	private TriangleMesh mesh;
	private Point3D position;
	private Point3D cible;
	private Point3D normeDeplacement;
	private double degresAngleDirection;
	
	
	public Mob(Point3D position, double degresAngleDirection){
		this.position = position;
		this.normeDeplacement = new Point3D(0,0,0);
		this.degresAngleDirection = degresAngleDirection;
		this.mobNode = new Group();
		this.mobNode.getChildren().addAll(new Box(100, 100, 100));
	}
	public Mob(Point3D position, Point3D cible){
		this.position = position;
		ciblerDestination(cible);
		this.mobNode = new Group();
		Box boite1 = new Box(5, 5, 5);
		this.mobNode.getChildren().addAll(boite1);
		boite1.setTranslateY(-boite1.getHeight()/2);
	}
	/**
	 * Cette fonction retourne l'objet Group contenant tous les �l�ments visuels pour dessiner le mob � l'�cran
	 * @return la Node contenant tous les �l�ments pour dessiner la node
	 */
	public Group getMobNode() {
		return mobNode;
	}
	
	/**
	 * Cette fonction configure l'objet {@link TriagnleMesh}
	 * @param mesh l'objet {@link TriangleMesh} � utiliser pour dessiner l'objet � l'�cran
	 */
	public void setMesh(TriangleMesh mesh){
		this.mesh = mesh;
	}
	/**
	 * Cette fonction retourne l'objet {@link TriangleMesh} de l'objet en question
	 * @return l'objet TriangleMesh n�cessaire � dessiner le mob � l'�cran
	 */
	public TriangleMesh getMesh(){
		return mesh;
	}
	/**
	 * Cette fonction d�finit la cible vers laquelle le mob va marcher
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
	 * cette fonction rafraichit l'�tat du mob.
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
	/**
	 * cette fonction d�place le mob
	 */
	private void deplacer(){
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
