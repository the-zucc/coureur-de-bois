package game;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

import app.App;
import characteristic.ComponentOwner;
import characteristic.Messageable;
import characteristic.Updateable;
import characteristic.positionnable.Collideable;
import entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import perlin.PerlinNoise;
import visual.Component;

public class Map implements ComponentOwner, Updateable, Messageable{
	
	private static Map instance;
	
	public static Map getInstance(){
		if(instance == null)
			instance = new Map(20000, 20000, 5000, 100, 100);
		return instance;
	}
	
	private ArrayList<Collideable> collideables;
	private ArrayList<Updateable> updateables;
	private ArrayList<Entity> entities;
	private ArrayList<ComponentOwner> componentOwners;
	
	private Point3D position;
	private Component component;
	
	private double mapWidth;
	private double mapHeight;
	private double vertexSeparationWidth;
	private double vertexSeparationHeight;
	
	private float[][] floorVertices;
	private Point3D[][][][] heightMatrix;
	
	public Map(double mapWidth, double mapHeight, double vertexSeparationWidth, double vertexSeparationHeight, int treeCount) {
		collideables = new ArrayList<Collideable>();
		updateables = new ArrayList<Updateable>();
		componentOwners = new ArrayList<ComponentOwner>();
		entities = new ArrayList<Entity>();
		
		componentOwners.add(this);
		
		//generating the terrain vertices
		int cols = (int)(mapWidth/vertexSeparationWidth);
		int rows = (int)(mapWidth/vertexSeparationHeight);
		floorVertices = generateFloorVertices(rows, cols);
		
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.vertexSeparationWidth = vertexSeparationWidth;
		this.vertexSeparationHeight = vertexSeparationHeight;
		
		setPosition(new Point3D(0,0,0));
		component = buildComponent();
		
	}
	
	public double getHeightAt(double x, double z) {
		return 0;
	}

	@Override
	public void setPosition(Point3D position) {
		this.position = position;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}
	@Override
	public Component buildComponent() {
		Component returnVal = new Component("map");
		returnVal.setPosition(position);
		returnVal.addChildComponent(buildFloorMesh());
		return returnVal;
	}
	private Component buildFloorMesh(){
		Component returnVal = new Component("id_floor_mesh");
		Point3D p1 = null;
		Point3D p2 = null;
		int cols = (int)(mapWidth/vertexSeparationWidth);
		for(double z  = mapHeight/2; z >= -mapHeight/2; z-= vertexSeparationHeight){
			TriangleMesh mesh = new TriangleMesh();
			for(double x = -mapWidth/2; x <= mapWidth/2; x += vertexSeparationWidth){
				p1 = new Point3D(x,0,z);
				p2 = new Point3D(x,0,z-vertexSeparationHeight);
				mesh.getPoints().addAll((float)p1.getX(), (float)p1.getY(), (float)p1.getZ());
				mesh.getPoints().addAll((float)p2.getX(), (float)p2.getY(), (float)p2.getZ());
			}
			mesh.getTexCoords().addAll(0,0);
			for(int i=2;i<cols*2;i+=2) {  //add each segment
		        //Vertices wound counter-clockwise which is the default front face of any Triange
		        //These triangles live on the frontside of the line facing the camera
		        mesh.getFaces().addAll(i,0,i-2,0,i+1,0); //add primary face
		        mesh.getFaces().addAll(i+1,0,i-2,0,i-1,0); //add secondary Width face
		        //waterMesh.getFaces().addAll(i,0,i-2,0,i+1,0); //add primary face
		        //waterMesh.getFaces().addAll(i+1,0,i-2,0,i-1,0); //add secondary Width face
		        //Add the same faces but wind them clockwise so that the color looks correct when camera is rotated
		        //These triangles live on the backside of the line facing away from initial the camera
		        mesh.getFaces().addAll(i+1,0,i-2,0,i,0); //add primary face
		        mesh.getFaces().addAll(i-1,0,i-2,0,i+1,0); //add secondary Width face
		    }
			MeshView floorMeshView = new MeshView(mesh);
			floorMeshView.setDrawMode(DrawMode.FILL);
			floorMeshView.setMaterial(new PhongMaterial(Color.GREEN));
			floorMeshView.setTranslateX(0);
			floorMeshView.setTranslateY(-7.5);
			floorMeshView.setTranslateZ(0);
			returnVal.getChildren().add(floorMeshView);
		}
		return returnVal;
	}
	private float[][] generateFloorVertices(int rows, int cols){
		
		PerlinNoise perlin = new PerlinNoise();
		int offsetX = ThreadLocalRandom.current().nextInt(450);
		int offsetY = 100;
		int offsetZ = 10;
		perlin.offset(offsetX,offsetY,offsetZ);
		//height matrix
		float[][] returnVal = new float[rows+1][cols+1];
		
		float multiplicator = 0.001f;
		//generate random heights
		for(int z = 0; z < rows+1; z++) {
			for(int x = 0; x < cols+1; x++) {
				float y = perlin.smoothNoise(x*multiplicator, z*multiplicator, 32, 24)*10000;
				returnVal[z][x]=y;
			}
		}
		return returnVal;
	}
	
	@Override
	public Component getComponent() {
		return component;
	}

	@Override
	public boolean isComponentInScene() {
		return getComponent().getParent() != null;
	}

	@Override
	public void placeComponentInScene() {
		App.getUserInterface().getGameScene().getGameRoot().getChildren().add(getComponent());
	}
	
	public ArrayList<ComponentOwner> getComponentOwners(){
		return componentOwners;
	}

	@Override
	public void update(double secondsPassed) {
		for(Updateable u:updateables){
			u.update(secondsPassed);	
		}
	}

	@Override
	public boolean shouldUpdate() {
		return true;
	}
	
	@Override
	public void onMessageReceived(Hashtable<String, ? extends Object> message) {
		if(message.containsKey("start")){
			Hashtable<String, Double> params = (Hashtable<String, Double>)message.get("start");
		}
	}
	
	public void addEntity(Entity e){
		if(e instanceof Updateable){
			updateables.add(((Updateable)e));
		}
		if(e instanceof Collideable){
			collideables.add((Collideable)e);
		}
		if(e instanceof ComponentOwner) {
			componentOwners.add((ComponentOwner)e);
		}
		entities.add(e);
		
	}
	
	/**
	 * COLLISIONS SECTION
	 */
	//static variables
	private static int collisionMapDivisionWidth=100;
	private static int collisionMapDivisionHeight=collisionMapDivisionWidth;
	
	public double getHeightAt(Point3D position){
		return 0;
	}
	
}
