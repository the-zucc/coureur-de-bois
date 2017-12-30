package entity;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;

public abstract class Mob {
	private TriangleMesh mesh;
	private Point3D position;
	private Point3D cible;
	private Point3D normeDeplacement;
	private double degresAngleDirection;
	
	
	public Mob(Point3D position, double degresAngleDirection){
		this.position = position;
		this.normeDeplacement = new Point3D(0,0,0);
		this.degresAngleDirection = degresAngleDirection;
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
	}
	/*
	 * Cette fonction rafraichit l'angle de rotation du mob
	 */
	public void rafraichirOrientation(){
		//le nouvel angle de direction
		Point2D coord2DDirection = new Point2D(normeDeplacement.getX(), normeDeplacement.getZ());
		degresAngleDirection = new Point2D(1, 0).angle(coord2DDirection);
	}
	public void animer(){
		deplacer();
	}
	private void deplacer(){
		if(position.distance(cible) < normeDeplacement.distance(Point3D.ZERO)){
			position = cible;
			normeDeplacement = Point3D.ZERO;
		}
		else
			position = position.add(normeDeplacement);
	}
}
