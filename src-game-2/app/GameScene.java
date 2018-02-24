package app;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

import entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import perlin.PerlinNoise;
import util.Updateable;
import visual.Component;
import visual.UpdateableComponent;

public class GameScene extends SubScene implements Updateable {
	//for singleton implementation
	private static GameScene instance;
	public static GameScene getInstance() {
		if(instance == null) {
			instance = new GameScene(new Group(), 1280, 720, SceneAntialiasing.BALANCED);
		}
		return instance;
	}
	public static GameScene newInstance(double width, double height, SceneAntialiasing antialiasing) {
		instance = new GameScene(new Group(), 1280, 720, antialiasing);
		return instance;
	}
	//instance variables
	private Hashtable<String, Component> gameComponentsHashtable;
	private ArrayList<Component> gameComponents;
	
	
	private Group gameEnvRoot;
	private PerspectiveCamera gameCamera;
	
	
	
	
	private static double setting_floorSectionWidth = 100;
	private static double floorSectionHeight = setting_floorSectionWidth;
	
	public static double getFloorSectionWidth() {
		return setting_floorSectionWidth;
	}
	public static double getFloorSectionHeight() {
		return floorSectionHeight;
	}
	
	private static float waterHeight = 100;
	public static double getWaterHeight() {
		return waterHeight;
	}
	
	
	private static Point3D[][][][] heightMatrix;
	public static Point3D[][][][] getHeightMatrix(){
		return heightMatrix;
	}
	
	private GameScene(Parent arg0, double arg1, double arg2, SceneAntialiasing arg4) {
		//create the scene
		super(arg0, arg1, arg2, true, arg4);
		
		//defining the gameComponents and the uINodes Hashtables
		gameComponentsHashtable = new Hashtable<String, Component>();
		gameComponents = new ArrayList<Component>();
		
		//defining custom class attributes
		
		
		gameEnvRoot = (Group)this.getRoot();//the root of the game environment (passed as argument to the subscene below)
								//it holds all the game elements' components
		/*
		PointLight light1 = new PointLight();
		light1.setColor(Color.ORANGE);
		light1.setRotate(45);
		
		PointLight light2 = new PointLight();
		light2.setColor(Color.RED);
		light2.setRotate(45);
		Group light2Group = new Group();
		light2Group.getChildren().addAll(new Sphere(50), light2);
		
		Group lightGroup = new Group();
		lightGroup.getChildren().addAll(light1, light2Group);
		lightGroup.setTranslateZ(0);
		
		gameEnvRoot.getChildren().add(lightGroup);
		*/
		gameCamera = buildGameCamera();//build the game camera
		
		setCamera(gameCamera);//setup the game scene to use the built camera
		
		//setup the floor
		Group floor = buildFloor((int)(Model.getInstance().getMapWidth()/setting_floorSectionWidth),(int)(Model.getInstance().getMapWidth()/floorSectionHeight), (int)setting_floorSectionWidth, (int)floorSectionHeight);
		floor.setId("id_floor");
		//add the floor to the game environment
		gameEnvRoot.getChildren().add(floor);
		
		//debug
		//gameEnvRoot.getChildren().add(new Label("2fdshajflhdsjaklhjkl"));
	}
	public void setupInitialComponents() {
		for(Entity e:Model.getInstance().getGameElements()) {
			updateComponentOfElement(e);
		}
	}
	
	/**
	 * updates the game components of the game subscene
	 */
	public void updateGraphics(double deltaTime) {
		Entity e;
		for(int i = 0; i < gameComponents.size(); i++) {
			Component gc = gameComponents.get(i);
			e = Model.getInstance().getEntity(gc.getId());
			if(e == null) {
				gameComponents.remove(gc);
				gameComponentsHashtable.remove(gc.getId());
				gameEnvRoot.getChildren().remove(gc);
			}
			else
				updateComponentOfElement(e);
			//((Updateable) component).update(deltaTime);
		}
	}
	
	/**
	 * updates the specified {@link Entity}'s corresponding element's position and state.
	 * @param ge the {@link Entity} of which to update the component
	 */
	private void updateComponentOfElement(Entity e) {
		Component component = gameComponentsHashtable.get(e.getId());
		if(component == null) {
			component = e.buildComponent();
			component.setId(e.getId());
			gameComponentsHashtable.put(component.getId(), component);
			gameComponents.add(component);
			gameEnvRoot.getChildren().add(component);
		}
		if(component instanceof UpdateableComponent) {
			((UpdateableComponent) component).update(e);
		}
	}
	
	/**
	 * returns a reference to the component corresponding to the specified component/element ID.
	 * @param id the id of the component
	 * @return the component corresponding to the specified id
	 */
	public Component getComponent(String id) {
		return gameComponentsHashtable.get(id);
	}
	
	@Override
	public void update(double deltaTime) {
		updateGraphics(deltaTime);
	}
	
	/**
	 * Builds the {@link PerspectiveCamera} of the game scene.
	 * @return the camera to add to the desired scene or subscene
	 */
	private static PerspectiveCamera buildGameCamera() {
		double distance = Controller.gameCameraDistance;
		PerspectiveCamera returnVal = new PerspectiveCamera(true);
		returnVal.setNearClip(0.1);
		returnVal.setFarClip(40000);
		returnVal.setTranslateY(-distance/3/*-distance*/);
		returnVal.setTranslateZ(-distance);
		returnVal.setRotationAxis(Rotate.X_AXIS);
		returnVal.setRotate(-17/*-90-45*/);
		return returnVal;
	}
	
	/**
	 * returns the game camera, to be able to modify the angle of the camera from anywhere within the program
	 * @return the {@link PerspectiveCamera} of the game environment.
	 */
	public PerspectiveCamera getGameCamera() {
		return gameCamera;
	}
	
	private static Group buildFloor(int cols, int rows, int colwidth, int rowheight) {
		
		PerlinNoise p = new PerlinNoise();
		p.offset(500,100,10);
		
		Group returnVal = new Group();
		
		double height = rowheight;
		double width = colwidth;
		
		double mapHeight = Model.getInstance().getMapHeight();
		double mapWidth = Model.getInstance().getMapWidth();
		
		//height matrix
		float[][] tempHeightMatrix = new float[rows+1][cols+1];
		float[][] waterHeightMatrix = new float[rows+1][cols+1];
		
		
		float multiplicator = 0.01f;
		//generate random heights
		for(int z = 0; z < rows+1; z++) {
			for(int x = 0; x < cols+1; x++) {
				float y = p.smoothNoise(x*multiplicator, z*multiplicator, 32, 24)*1000;
				tempHeightMatrix[z][x]=y;
				waterHeightMatrix[z][x]=(float)ThreadLocalRandom.current().nextDouble()*25;
			}
		}
		
		//2D array with two triangle per "cell"
		heightMatrix = new Point3D[rows][cols][2][3];
		
		for(int z = 0; z < rows-1; z++) {
			int zIndex = z;
			int zCoordRef = -z;
			TriangleMesh mesh = new TriangleMesh();
			TriangleMesh waterMesh = new TriangleMesh();
			
			for(int x = 0; x < cols; x++) {
				
				//top left triangle
				heightMatrix[zIndex][x][0][0] = new Point3D(x*width-mapWidth/2, tempHeightMatrix[zIndex][x], mapHeight/2+zCoordRef*height);
				heightMatrix[zIndex][x][0][1] = new Point3D((x+1)*width-mapWidth/2, tempHeightMatrix[zIndex][x+1], mapHeight/2+zCoordRef*height);
				heightMatrix[zIndex][x][0][2] = new Point3D(x*width-mapWidth/2, tempHeightMatrix[zIndex+1][x], mapWidth/2+(zCoordRef-1)*height);
				
				//bottom right triangle
				heightMatrix[zIndex][x][1][0] = new Point3D((x+1)*width-mapWidth/2, tempHeightMatrix[zIndex+1][x+1], mapWidth/2+(zCoordRef-1)*height);
				heightMatrix[zIndex][x][1][1] = new Point3D(x*width-mapWidth/2, tempHeightMatrix[zIndex+1][x], mapWidth/2+(zCoordRef-1)*height);
				heightMatrix[zIndex][x][1][2] = new Point3D((x+1)*width-mapWidth/2, tempHeightMatrix[zIndex][x+1], mapWidth/2+zCoordRef*height);
				
				/*
				if(x < cols-1) {
					heightMatrix[z][x][0][0] = new Point3D((-x*width)-(-Model.getInstance().getMapWidth()/2), tempHeightMatrix[z][x], ((-z)*height)-(-Model.getInstance().getMapHeight()/2));
					heightMatrix[z][x][0][1] = new Point3D((-(x+1)*width)-(-Model.getInstance().getMapWidth()/2), tempHeightMatrix[z][x+1], ((-z)*height)-(-Model.getInstance().getMapHeight()/2));
					heightMatrix[z][x][0][2] = new Point3D((-x*width)-(-Model.getInstance().getMapWidth()/2), tempHeightMatrix[z+1][x], ((-(z+1))*height)-(-Model.getInstance().getMapHeight()/2));
					
				}
				if(x >= 1){
					heightMatrix[z][x][1][0] = new Point3D((-x*width)-(-Model.getInstance().getMapWidth()/2), tempHeightMatrix[z+1][x], ((-(z+1))*height)-(-Model.getInstance().getMapHeight()/2));
					heightMatrix[z][x][1][1] = new Point3D((-(x-1)*width)-(-Model.getInstance().getMapWidth()/2), tempHeightMatrix[z+1][x-1], ((-z)*height)-(-Model.getInstance().getMapHeight()/2));
					heightMatrix[z][x][1][2] = new Point3D((-x*width)-(-Model.getInstance().getMapWidth()/2), tempHeightMatrix[z][x], ((-z)*height)-(-Model.getInstance().getMapHeight()/2));
					//System.out.println("next triangle odd");
				}*/
				for(Point3D[] ps:heightMatrix[zIndex][x]){
					//System.out.println("triangle:");
					for(Point3D p1:ps){
						//System.out.println(p1);
					}
					
				}
				/*
				System.out.println("===");
				
				//(float)((-x*width)-(-Model.getInstance().getMapWidth()/2)), tempHeightMatrix[z][x], (float)(((-z)*height)-(-Model.getInstance().getMapHeight()/2))
				//(float)((-x*width)-(-Model.getInstance().getMapWidth()/2)), tempHeightMatrix[z+1][x], (float)(((-(z+1))*height)-(-Model.getInstance().getMapHeight()/2))
				*/
				Point3D p1 = heightMatrix[zIndex][x][0][0];
				//System.out.println("p1: "+p1);
				Point3D p2 = heightMatrix[zIndex][x][0][2];
				//System.out.println("p2: "+p2);
				//Point3D p3 = heightMatrix[zIndex][x][0][2];
				
				
				mesh.getPoints().addAll((float)p2.getX(), (float)p2.getY(), (float)p2.getZ());
				mesh.getPoints().addAll((float)p1.getX(), (float)p1.getY(), (float)p1.getZ());
				waterMesh.getPoints().addAll((float)p2.getX(), waterHeight, (float)p2.getZ());
				waterMesh.getPoints().addAll((float)p1.getX(), waterHeight, (float)p1.getZ());
				//mesh.getPoints().addAll((float)p3.getX(), (float)p3.getY(), (float)p3.getZ());
				//GameScene.getInstance().createConnection(Point3D.ZERO, new Point3D((float)((-x*width)-(-Model.getInstance().getMapWidth()/2)), tempHeightMatrix[z][x], (float)(((-z)*height)-(-Model.getInstance().getMapHeight()/2))), null);
			}
			mesh.getTexCoords().addAll(0,0);
			waterMesh.getTexCoords().addAll(0,0);
			for(int i=2;i<cols*2;i+=2) {  //add each segment
		        //Vertices wound counter-clockwise which is the default front face of any Triange
		        //These triangles live on the frontside of the line facing the camera
		        //mesh.getFaces().addAll(i,0,i-2,0,i+1,0); //add primary face
		        //mesh.getFaces().addAll(i+1,0,i-2,0,i-1,0); //add secondary Width face
		        //waterMesh.getFaces().addAll(i,0,i-2,0,i+1,0); //add primary face
		        //waterMesh.getFaces().addAll(i+1,0,i-2,0,i-1,0); //add secondary Width face
		        //Add the same faces but wind them clockwise so that the color looks correct when camera is rotated
		        //These triangles live on the backside of the line facing away from initial the camera
		        mesh.getFaces().addAll(i+1,0,i-2,0,i,0); //add primary face
		        mesh.getFaces().addAll(i-1,0,i-2,0,i+1,0); //add secondary Width face
		        waterMesh.getFaces().addAll(i+1,0,i-2,0,i,0); //add primary face
		        waterMesh.getFaces().addAll(i-1,0,i-2,0,i+1,0); //add secondary Width face
		    }
			MeshView meshView = new MeshView(mesh);
			meshView.setDrawMode(DrawMode.FILL);
			PhongMaterial material = new PhongMaterial(Color.WHITE);
			
			MeshView waterMeshView = new MeshView(waterMesh);
			waterMeshView.setMaterial(new PhongMaterial(new Color(0,0.05,0.7,0.5)));
			meshView.setMaterial(material);
			meshView.setTranslateX(0);
			meshView.setTranslateY(-7.5);
			meshView.setTranslateZ(0);
			returnVal.getChildren().addAll(meshView, waterMeshView);
		}
		return returnVal;
	}
	
	public void setCameraOnPlayer(Camera camera, String id) {
		gameComponentsHashtable.get(id).getChildren().add(camera);
	}
	
	public void addUINode(Node node) {
		
	}
	
	public void createConnection(Point3D origin, Point3D target, PhongMaterial material) {
	    Point3D yAxis = new Point3D(0, 1, 0);
	    Point3D diff = target.subtract(origin);
	    double height = diff.magnitude();

	    Point3D mid = target.midpoint(origin);
	    Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

	    Point3D axisOfRotation = diff.crossProduct(yAxis);
	    double angle = Math.acos(diff.normalize().dotProduct(yAxis));
	    Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

	    Cylinder line = new Cylinder(1, height);

	    line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
	    line.setMaterial(material);

	    ((Group)this.getRoot()).getChildren().addAll(line);
	}
	public void addMeshView(Point3D[][] triangles){
		for(Point3D[] triangle:triangles){
			TriangleMesh mesh = new TriangleMesh();
			
			for(Point3D p:triangle){
				mesh.getPoints().addAll((float)p.getX(), (float)p.getY(), (float)p.getZ());
			}
			mesh.getFaces().addAll(2,0,1,1,0,2);
			mesh.getTexCoords().addAll(0,0);
			//System.out.println(triangle);
			MeshView meshview = new MeshView(mesh);
			meshview.setDrawMode(DrawMode.FILL);
			
			((Group)getRoot()).getChildren().add(meshview);
		}
	}
}
