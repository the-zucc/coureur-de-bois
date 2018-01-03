package dao;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.DoubleStream;

import entity.*;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import view.View;

public class MobsDAO {
	private static MobsDAO instance;
	private ArrayList<Mob> mobs;
	private Player currentPlayer;
	private Point3D vectDeplacementJoueur;
	private Group root;
	
	private MobsDAO(){
		vectDeplacementJoueur = new Point3D(0,0,0);
		mobs = new ArrayList<Mob>();
		//currentPlayer = new Player(1, "Laurier", new Point3D(0,0,0));
	}
	private MobsDAO(int nombreMobsTest) {
		vectDeplacementJoueur = new Point3D(0,0,0);
		mobs = new ArrayList<Mob>();
		//currentPlayer = new Player(1, "Laurier", new Point3D(0,0,0));
		//View.getInstance().getCurrentRoot().getChildren().add(currentPlayer.getPlayerNode());
		for(int i = 0; i < nombreMobsTest; i++) {
			int min = -500;
			int max = 500;
			double x = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			double z = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			Point3D pos = new Point3D(x, 0, z);
			double cibleX = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			double cibleZ = (double)ThreadLocalRandom.current().nextInt(min, max + 1);
			Point3D cib = new Point3D(cibleX, 0, cibleZ);
			mobs.add(new Mob1(pos, cib));
			System.out.println("ajout mob"+i);
			View.getInstance().getCurrentRoot().getChildren().add(mobs.get(i).getMobNode());
		}
	}
	public static MobsDAO getInstance(){
		if(instance == null){
			instance = new MobsDAO(); 
		}
		return instance;
	}
	public static MobsDAO getInstance(int nombreMobsTest){
		if(instance == null){
			instance = new MobsDAO(nombreMobsTest); 
		}
		return instance;
	}
	public void setDirectionJoueur(Direction dir) {
		currentPlayer.setDirection(dir);
	}
	
	
	/**
	 * Cette fonction rafraichit le modèle.
	 */
	public void refresh() {
		for(Mob m:mobs) {
			m.refresh();
		}
		//should refresh the player here
		//currentPlayer.refresh();
	}
	/**
	 * cette fonction retourne un {@link ArrayList} contenant tous les mobs du jeu
	 * @return l'ArrayList des mobs
	 */
	public ArrayList<Mob> getMobs(){
		return mobs;
	}
	/**
	 * cette fonction retourne le nombre de mobs présents dans le jeu
	 * @return le nombre de mobs
	 */
	public int getMobCount() {
		return mobs.size();
	}
	
}
