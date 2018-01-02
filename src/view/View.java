package view;
import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;
import java.util.Hashtable;

import dao.MobsDAO;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class View extends Application{
	private static View instance;
	
	private int indexBoiteDebug;
	private Point3D normeDeplacementJoueur;
	
	private Hashtable<String, Group> roots;
	private Hashtable<String, Scene> scenes;
	private Hashtable<String, Camera> cameras;
	
	private Scene currentScene;
	private Group currentRoot;
	private Camera currentCamera;
	
	private Stage stage;
	
	public static void main(String[] args) {
		View.launch(args);
	}
	
	public static View getInstance(){
		return instance;
	}
	
	@Override
	public void start(Stage arg0) throws Exception {
		instance=this;
		this.stage = arg0;//pour rendre la fenêtre accessible de n'importe où

		//initialisation de la liste des modes de caméra
		roots = new Hashtable<String, Group>();
		scenes = new Hashtable<String, Scene>();
		cameras = new Hashtable<String, Camera>();
		//création du mode principal
		String modePrincipal = "principal";
		roots.put(modePrincipal, new Group());
		scenes.put(modePrincipal, new Scene(roots.get(modePrincipal), 1366, 768, true, SceneAntialiasing.BALANCED));
		cameras.put(modePrincipal, new PerspectiveCamera(true));
		
		//configuration de la scene sur le mode principal		
		setMode("principal");
		currentCamera.setTranslateZ(-1000);
		currentCamera.setNearClip(0.1);
		currentCamera.setFarClip(3000.0);
		((PerspectiveCamera) currentCamera).setFieldOfView(60);
		currentScene.setCamera(currentCamera);
		Box box1 = new Box(100, 100, 100);
		//currentRoot.getChildren().addAll(new Rectangle(0, 0, 100, 100));
		//Rectangle rect = new Rectangle(0, 0, 200, 200);
		//rect.setFill(Color.BLUE);
		Box plancher = new Box(1000, 1, 1000);
		currentRoot.getChildren().addAll(box1, plancher);
		
		Line ligneX = new Line(0, -100, 0, 100);
		Line ligneY = new Line(100, 0, -100, 0);
		ligneY.setFill(Color.RED);
		View.this.currentRoot.getChildren().addAll(ligneX, ligneY);
		MobsDAO.getInstance(10);
		currentScene.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Node node = arg0.getPickResult().getIntersectedNode();
			}
			
		});
		indexBoiteDebug = 0;
		currentScene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent arg0) {
				double norme = 1;
				
				KeyCode code = arg0.getCode();
				if(code.equals(KeyCode.W)) {
					System.out.println(code);
					MobsDAO.getInstance().setVectDeplacementJoueur(new Point3D(0, 0, norme));
				}else if (code.equals(KeyCode.S)) {
					MobsDAO.getInstance().setVectDeplacementJoueur(new Point3D(0, 0, -norme));
				}else if (code.equals(KeyCode.A)) {
					MobsDAO.getInstance().setVectDeplacementJoueur(new Point3D(-norme, 0, 0));
				}else if (code.equals(KeyCode.D)) {
					MobsDAO.getInstance().setVectDeplacementJoueur(new Point3D(norme, 0, 0));
				}
			}
		});
		Animation animation = new Transition() {
			{
				setCycleDuration(Duration.millis(500));
			}
			protected void interpolate(double frac) {
				double isometricValue = -500;
				Camera currentCamera = View.this.currentCamera;
				
				//currentCamera.setTranslateX(isometricValue*frac);
				currentCamera.setTranslateY(isometricValue*frac);
				//currentCamera.setTranslateZ(isometricValue*frac);
				
				//currentCamera.setRotationAxis(Rotate.Y_AXIS);
				//currentCamera.setRotate(80*frac);
				currentCamera.setRotationAxis(Rotate.X_AXIS);
				currentCamera.setRotate(-45*frac);
				//currentCamera.setRotationAxis(Rotate.Z_AXIS);
				//currentCamera.setRotate(0*frac);
			}
		};
		animation.play();
		
		ControlerTimer timer = new ControlerTimer();
		timer.start();
		arg0.show();
	}
	public Scene getCurrentScene(){
		return currentScene;
	}
	public Group getCurrentRoot(){
		return currentRoot;
	}
	public void setMode(String nomMode){
		currentScene = scenes.get(nomMode);
		currentRoot = roots.get(nomMode);
		currentCamera = cameras.get(nomMode);
		stage.setScene(currentScene);
	}
}
