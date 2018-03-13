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
import entity.living.human.Player;
import entity.statics.tree.TreeNormal;
import game.settings.Preferences;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import perlin.PerlinNoise;
import ui.gamescene.GameCamera;
import visual.Component;

public class Map implements ComponentOwner, Updateable, Messageable{
	
	private static Map instance;
	
	public static Map getInstance(){
		if(instance == null){
			instance = new Map(Preferences.getMapWidth(), Preferences.getMapHeight(), Preferences.getTreeCount(), 100, 100);
			System.out.println("height: "+Preferences.getMapHeight());
			System.out.println("width: "+Preferences.getMapWidth());
		}
		return instance;
	}
	
	private ArrayList<Collideable> collideables;
	private ArrayList<Updateable> updateables;
	private ArrayList<Entity> entities;
	private ArrayList<ComponentOwner> componentOwners;
	private Player currentPlayer;
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	
	private Point3D position;
	private Component component;
	private GameCamera gameCamera;
	
	private double mapWidth;
	private double mapHeight;
	private double vertexSeparationWidth;
	private double vertexSeparationHeight;
	
	private float[][] heightMatrix;
	private Point3D[][] floorVertices;
	
	public Map(double mapWidth, double mapHeight, int treeCount, double vertexSeparationWidth, double vertexSeparationHeight) {
		collideables = new ArrayList<Collideable>();
		updateables = new ArrayList<Updateable>();
		componentOwners = new ArrayList<ComponentOwner>();
		entities = new ArrayList<Entity>();
		
		componentOwners.add(this);
		
		//generating the terrain vertices
		int cols = (int)(mapWidth/vertexSeparationWidth);
		int rows = (int)(mapWidth/vertexSeparationHeight);
		heightMatrix = generateHeightMatrix(rows, cols);
		floorVertices = new Point3D[rows][cols];
		
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		this.vertexSeparationWidth = vertexSeparationWidth;
		this.vertexSeparationHeight = vertexSeparationHeight;
		
		setPosition(new Point3D(0,0,0));
		component = buildComponent();
		
		currentPlayer = new Player(new Point3D(0,0,0));
		addEntity(currentPlayer);
		for (int i = 0; i < treeCount; i++) {
			double x = Math.random()*mapWidth-mapWidth/2;
			double z = Math.random()*mapHeight-mapHeight/2;
			double y = getHeightAt(new Point3D(x,0,z));
			addEntity(new TreeNormal(new Point3D(x,y,z)));
		}

	}
	
	public double getHeightAt(Point3D arg0){
		try {
			double rowHeight = vertexSeparationHeight;
			double rowWidth = vertexSeparationWidth;
			
			
			int row = (int)((-arg0.getZ()+mapHeight/2)/rowHeight);
			int column = (int)((arg0.getX()+mapWidth/2)/rowWidth);
			
			
			
			int xmod = (int) (arg0.getX()%vertexSeparationWidth);
			if(xmod < 0){
				xmod+=vertexSeparationWidth;
			}
			
			int ymod = (int) (arg0.getZ()%vertexSeparationHeight);
			if(ymod < 0){
				ymod+=vertexSeparationHeight;
			}
			
			Point3D[] triangle = new Point3D[3];
			if(xmod < ymod) {
				triangle[0] = floorVertices[row][column];
				triangle[1] = floorVertices[row+1][column];
				triangle[2] = floorVertices[row][column+1];
			}
			else{
				triangle[0] = floorVertices[row+1][column+1];
				triangle[1] = floorVertices[row+1][column];
				triangle[2] = floorVertices[row][column+1];
			}
			Point3D p0 = triangle[0];
			
			Point3D vect1 = p0.subtract(triangle[1]);
			Point3D vect2 = p0.subtract(triangle[2]);

			Point3D normal = vect1.crossProduct(vect2).normalize();
			
			double arg0p0x = arg0.getX()-p0.getX();

			double arg0p0z = arg0.getZ()-p0.getZ();
			
			double returnVal = ((normal.getX() * (arg0p0x) + normal.getZ() * (arg0p0z)/* - letD*/)/-normal.getY()) + p0.getY();
			/*
			if(returnVal > GameScene.getWaterHeight())
				returnVal = GameScene.getWaterHeight();
			*/
			return returnVal;
		}catch(Exception e) {
			
			//System.out.println("exception caught, fix needed");
			return 0;
		}
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
		Component returnVal = new Component("id_map");
		returnVal.setPosition(position);
		returnVal.addChildComponent(buildFloorMesh());
		returnVal.addChildComponent(buildWaterMesh(Preferences.getWaterLevel()));

		return returnVal;
	}
	private Component buildFloorMesh(){
		Component returnVal = new Component("id_floor_mesh");
		Point3D p1 = null;
		Point3D p2 = null;
		int cols = (int)(mapWidth/vertexSeparationWidth);
		int zi=0;
		int xi=0;
		for(double z  = mapHeight/2; z > -mapHeight/2; z-= vertexSeparationHeight, zi++){
			TriangleMesh mesh = new TriangleMesh();
			xi=0;
			for(double x = -mapWidth/2; x < mapWidth/2; x += vertexSeparationWidth, xi++){
				p1 = new Point3D(x,heightMatrix[zi][xi],z);
				floorVertices[zi][xi] = p1;
				p2 = new Point3D(x,heightMatrix[zi+1][xi],z-vertexSeparationHeight);
				//System.out.println("zi:"+zi+" z:"+z+" xi:"+xi+" x:"+x);
				
				mesh.getPoints().addAll((float)p2.getX(), (float)p2.getY(), (float)p2.getZ());
				mesh.getPoints().addAll((float)p1.getX(), (float)p1.getY(), (float)p1.getZ());
			}
			mesh.getTexCoords().addAll(0,0);
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
	private Component buildWaterMesh(float waterLevel){
	    Component returnVal = new Component("id_water_mesh");

	    TriangleMesh mesh = new TriangleMesh();

        mesh.getPoints().addAll((float)-mapWidth/2, waterLevel, (float)mapHeight/2);
        mesh.getPoints().addAll((float)-mapWidth/2, waterLevel, (float)-mapHeight/2);
        mesh.getPoints().addAll((float)mapWidth/2, waterLevel, (float)mapHeight/2);
        mesh.getPoints().addAll((float)mapWidth/2, waterLevel, (float)-mapHeight/2);

		//mesh.getFaces().addAll(0,0,2,0,1,0); //add primary face
		mesh.getFaces().addAll(0,0,1,0,2,0); //add secondary Width face
		mesh.getFaces().addAll(3,0,2,0,1,0);
		//mesh.getFaces().addAll(2,0,0,0,1,0); //add primary face
		//mesh.getFaces().addAll(1,0,0,0,3,0); //add secondary Width face

		mesh.getTexCoords().addAll(0,0);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(new PhongMaterial(new Color(0,0,0.5,0.6)));
		returnVal.getChildren().add(meshView);
	    return returnVal;
    }
	private float[][] generateHeightMatrix(int rows, int cols){
		
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
				//System.out.println("z:"+z+"x:"+x);
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
		//return getComponent().getParent() != null;
		return true;
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
		for(ComponentOwner c:componentOwners){
			if(!c.isComponentInScene()) {
				if (c instanceof TreeNormal) {
					System.out.println("tree");
					System.out.println(c.getPosition());
				}
				getComponent().addChildComponent(c.getComponent());
			}
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
	/*
	public double getHeightAt(Point3D arg0){
		try {
			
			double rowHeight = vertexSeparationHeight;
			double rowWidth = vertexSeparationWidth;
			
			
			int row = (int)((-arg0.getZ()+mapHeight/2)/rowHeight);
			int column = (int)((arg0.getX()+mapWidth/2)/rowWidth);
			
			Point3D[][] currentTwoTriangles = floorVertices[row][column];
			
			int xmod = (int) (arg0.getX()%vertexSeparationWidth);
			if(xmod < 0){
				xmod+=vertexSeparationWidth;
			}
			
			int ymod = (int) (arg0.getZ()%vertexSeparationHeight);
			if(ymod < 0){
				ymod+=vertexSeparationHeight;
			}
			
			Point3D[] currentTriangle = null;
			if(xmod < ymod) {
				currentTriangle = currentTwoTriangles[0];
			}
			else{
				currentTriangle = currentTwoTriangles[1];
			}
			Point3D p0 = currentTriangle[0];
			
			Point3D vect1 = p0.subtract(currentTriangle[1]);
			Point3D vect2 = p0.subtract(currentTriangle[2]);

			Point3D normal = vect1.crossProduct(vect2).normalize();
			
			double arg0p0x = arg0.getX()-p0.getX();

			double arg0p0z = arg0.getZ()-p0.getZ();
			
			double returnVal = ((normal.getX() * (arg0p0x) + normal.getZ() * (arg0p0z))/-normal.getY()) + p0.getY();
			
			if(returnVal > GameScene.getWaterHeight())
				returnVal = GameScene.getWaterHeight();
			
			return returnVal;
		}catch(Exception e) {
			
			//System.out.println("exception caught, fix needed");
			return 0;
		}
	}
	*/
	public void setGameCamera(GameCamera gc){
		gameCamera = gc;
		updateables.add(gc);
	}
}
