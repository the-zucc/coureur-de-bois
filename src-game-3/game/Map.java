package game;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import app.App;
import characteristic.ComponentOwner;
import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.Updateable;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.Positionnable2D;
import entity.Entity;
import entity.MovingCollidingEntity;
import entity.living.animal.Pig;
import entity.living.human.Player;
import entity.statics.tree.PineTree;
import entity.statics.village.Tipi;
import entity.statics.tree.TreeNormal;
import game.settings.Preferences;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import perlin.PerlinNoise;
import ui.gamescene.GameCamera;
import util.PositionGenerator;
import village.Village;
import visual.Component;

public class Map implements ComponentOwner, Updateable {
	
	private static Map instance;
	
	public static Map getInstance() {
		if (instance == null) {
			instance = new Map(Preferences.getMapWidth(),
					Preferences.getMapHeight(),
					Preferences.getMapDetail(),
					Preferences.getMapDetail(),
					Preferences.getWaterLevel(),
					Preferences.getTreeCount(),
					Preferences.getVillageCount(),
					Preferences.getVillageRadius(),
					Preferences.getVillageTipiCount(),
					Preferences.getVillageVillagerCount(),
					1000);
		}
		return instance;
	}

	private Player currentPlayer;
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	
	private Point3D position;
	@Override
	public void setPosition(Point3D position) {
		this.position = position;
	}

	@Override
	public Point3D getPosition() {
		return position;
	}
	private Component component;
	@Override
	public Component getComponent() {
		return component;
	}
	@Override
	public Component buildComponent() {
		Component returnVal = new Component("id_map");
		returnVal.setPosition(position);
		returnVal.addChildComponent(buildFloorMesh());
		returnVal.addChildComponent(buildWaterMesh((float)waterLevel));

		return returnVal;
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


	private GameCamera gameCamera;
	private double mapWidth;
	private double mapHeight;
	public double getMapWidth(){
		return mapWidth;
	}
	public double getMapHeight(){
		return mapHeight;
	}
	private double vertexSeparationWidth;
	private double vertexSeparationHeight;
	
	private float[][] heightMatrix;
	private Point3D[][] floorVertices;
	private double waterLevel;

	private ArrayList<Collideable> collideables;
	private ArrayList<Updateable> updateables;
	private ArrayList<Entity> entities;
	private ArrayList<ComponentOwner> componentOwners;
	private ArrayList<Village> villages;
	
	private Messenger messenger;

	public Map(double mapWidth,
			double mapHeight,
			double vertexSeparationWidth,
			double vertexSeparationHeight,
			double waterLevel,
			int treeCount,
			int villageCount,
			double villageRadius,
			int tipiCount,
			int villagerCount,
			int sheepCount)
	{
		messenger = createMessenger();
		collideables = new ArrayList<Collideable>();
		collisionCols = (int)mapWidth/collisionMapDivisionWidth;
		collisionRows = (int)mapHeight/collisionMapDivisionHeight;
		collisionMap = new ArrayList[collisionRows][collisionRows];
		for(int i = 0; i < collisionRows; i++){
			for(int j = 0; j < collisionCols; j++){
				collisionMap[i][j] = new ArrayList<Collideable>();
			}
		}
		updateables = new ArrayList<Updateable>();
		componentOwners = new ArrayList<ComponentOwner>();
		entities = new ArrayList<Entity>();
		villages = new ArrayList<Village>();
		
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

		this.waterLevel = waterLevel;
		component = buildComponent();

		currentPlayer = new Player(PositionGenerator.generateRandom3DPositionOnFloor(this), this);
		addEntity(currentPlayer);

		for (int i = 0; i < treeCount; i++) {
			Point3D pos = PositionGenerator.generateRandom3DPositionOnFloor(this);
			TreeNormal tree;
			double val = Math.random();
			if(val > 0.5){
				tree = new PineTree(pos, this);
			}
			else{
				tree = new TreeNormal(pos,this);
			}
			addEntity(tree);
		}
		for (int i = 0; i < tipiCount; i++) {
			Point3D pos = PositionGenerator.generateRandom3DPositionOnFloor(this);
			addEntity(new Tipi(pos, this));
		}
		for(int i= 0; i < villageCount; i++){
			Point2D villagePos = PositionGenerator.generate2DPositionNotInVillages(this, villages);
			Village v = new Village(villagePos, tipiCount, 2000,villagerCount, this);
			villages.add(v);
			v.addEntitiesToMap(this);
		}
		for(int i = 0; i < sheepCount; i++){
			addEntity(new Pig(PositionGenerator.generateRandom3DPositionOnFloor(this), this));
		}
	}

	private Messenger createMessenger() {
		return new Messenger() {
			private ArrayList<MessageReceiver> receivers = new ArrayList<MessageReceiver>();
			@Override
			public void notifyReceivers(String message) {
			    for(MessageReceiver r:getReceivers()){
			        r.receiveMessage(message);
                }
			}

			@Override
			public void notifyReceivers(String message, Object... params) {
                for(MessageReceiver r:getReceivers()){
                    r.receiveMessage(message, params);
                }
			}

			@Override
			public void send(String message) {
				notifyReceivers(message);
			}

            @Override
            public void send(String message, Object... params) {
                notifyReceivers(message, params);
            }

			@Override
			public ArrayList<MessageReceiver> getReceivers() {
				return receivers;
			}

			@Override
			public void addReceiver(MessageReceiver o) {
				receivers.add(o);
			}

			@Override
			public void removeReceiver(MessageReceiver o) {
				receivers.remove(o);
			}
		};
	}

	public double getHeightAt(Point2D arg0){
		try {
			double rowHeight = vertexSeparationHeight;
			double rowWidth = vertexSeparationWidth;
			
			
			int row = (int)((-arg0.getY()+mapHeight/2)/rowHeight);
			int column = (int)((arg0.getX()+mapWidth/2)/rowWidth);
			
			int xmod = (int) ((arg0.getX()+mapWidth/2)%rowWidth);
			
			int ymod = (int) (rowHeight-((-arg0.getY()+mapHeight/2)%rowHeight));
			
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

			double arg0p0z = arg0.getY()-p0.getZ();
			
			double returnVal = ((normal.getX() * (arg0p0x) + normal.getZ() * (arg0p0z)/* - letD*/)/-normal.getY()) + p0.getY();
			/*
			if(returnVal > GameScene.getWaterHeight())
				returnVal = GameScene.getWaterHeight();
			*/
			//App.getUserInterface().getGameScene().createConnection(triangle[0], triangle[1], new PhongMaterial(Color.BLACK));
			//App.getUserInterface().getGameScene().createConnection(triangle[1], triangle[2], new PhongMaterial(Color.BLACK));
			//App.getUserInterface().getGameScene().createConnection(triangle[2], triangle[0], new PhongMaterial(Color.BLACK));
			return returnVal;
		}catch(Exception e) {
			
			//System.out.println("exception caught, fix needed");
			return 0;
		}
	}


	private Component buildFloorMesh(){
		Component returnVal = new Component("id_floor_mesh");
		Point3D p1;
		int cols = (int)(mapWidth/vertexSeparationWidth);
		int rows = (int)(mapHeight/vertexSeparationHeight);
		int zi=0;
		int xi;
		TriangleMesh mesh = new TriangleMesh();
		for(double z  = mapHeight/2; z > -mapHeight/2; z-= vertexSeparationHeight, zi++){
			xi=0;
			for(double x = -mapWidth/2; x < mapWidth/2; x += vertexSeparationWidth, xi++){
				p1 = new Point3D(x,heightMatrix[zi][xi],z);
				floorVertices[zi][xi] = p1;
				//System.out.println("zi:"+zi+" z:"+z+" xi:"+xi+" x:"+x);
				mesh.getPoints().addAll((float)p1.getX(), (float)p1.getY(), (float)p1.getZ());
				if(zi<rows-1 && xi < cols-1){
					int idx = xi+(zi*cols);
					//System.out.println("idx:"+idx);
					//System.out.println("idx+cols:"+(idx+cols));
					mesh.getFaces().addAll(idx+1,0,idx,0,idx+cols,0);
					mesh.getFaces().addAll(idx+cols,0,idx+cols+1,0,idx+1,0);
				}
			}
		}
		//System.out.println(((mesh.getPoints().size()/3)-1)+" points");
		mesh.getTexCoords().addAll(0,0);

		MeshView floorMeshView = new MeshView(mesh);
		floorMeshView.setDrawMode(DrawMode.FILL);
		floorMeshView.setMaterial(new PhongMaterial(Color.GREEN));
		floorMeshView.setTranslateX(0);
		floorMeshView.setTranslateZ(0);
		returnVal.getChildren().add(floorMeshView);
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
		int offsetX = ThreadLocalRandom.current().nextInt(900);
		int offsetY = 100;
		int offsetZ = 10;
		perlin.offset(offsetX,offsetY,offsetZ);
		//height matrix
		float[][] returnVal = new float[rows+1][cols+1];
		
		float multiplicator = 0.001f;
		//generate random heights
		for(int z = 0; z < rows+1; z++) {
			for(int x = 0; x < cols+1; x++) {
				float y = perlin.smoothNoise(x*multiplicator, z*multiplicator, 32, 24)*(float)(1000*GameLogic.getMeterLength());
				returnVal[z][x]=y;
			}
		}
		return returnVal;
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

    public void addEntity(Entity e){
		if(e instanceof Updateable){
			updateables.add(((Updateable)e));
		}
		if(e instanceof Collideable){
			collideables.add((Collideable)e);
			Point2D pos = ((Collideable) e).get2DPosition();
			int row = getCollisionRowFor(pos);
			int col = getCollisionColumnFor(pos);
			try{
				collisionMap[row][col].add((Collideable)e);
				if(e instanceof MovingCollidingEntity){
					((MovingCollidingEntity) e).setCollisionMapRow(row);
					((MovingCollidingEntity) e).setCollisionMapColum(col);
				}
			}catch(ArrayIndexOutOfBoundsException aioobe){
				
			}
		}
		if(e instanceof ComponentOwner) {
			componentOwners.add((ComponentOwner)e);
			getComponent().addChildComponent(((ComponentOwner)e).getComponent());
		}
		entities.add(e);
	}
	@Override
	public Point2D compute2DPosition(Point3D position3d) {
		return Point2D.ZERO;
	}

	@Override
	public void set2DPosition(Point2D position2d) {
	}

	@Override
	public Point2D get2DPosition() {
		return Point2D.ZERO;
	}

	@Override
	public double distanceFrom(Positionnable2D arg0) {
		return Point2D.ZERO.distance(arg0.get2DPosition());
	}

	public double getWaterLevel() {
		return waterLevel;
	}

	public void setGameCamera(GameCamera gc){
		gameCamera = gc;
		updateables.add(gc);
	}

	/**
	 * COLLISIONS
	 */
	//static variables
	private static int collisionMapDivisionWidth=(int)(10*GameLogic.getMeterLength());
	private static int collisionMapDivisionHeight=collisionMapDivisionWidth;
	private int collisionCols;
	private int collisionRows;
	
	public static int getCollisionMapDivisionWidth(){
		return collisionMapDivisionWidth;
	}
	public static int getCollisionMapDivisionHeight(){
		return collisionMapDivisionHeight;
	}
	private ArrayList<Collideable>[][] collisionMap;
	private ArrayList<Collideable> bigCollideables;
	public ArrayList<Collideable> getBigCollideables(){
		return bigCollideables;
	}
	
	public ArrayList<Collideable>[][] getCollisionMap(){
		return collisionMap;
	}
	public ArrayList<Collideable>[][] getNearbyCollideables(int row, int col){
		ArrayList<Collideable>[][] returnVal = new ArrayList[3][3];
		int ri = 0;
		for(int i = row-1; i <= row+1; i++, ri++){
			int rj = 0;
			for(int j = col-1; j <= col+1; j++, rj++){
				returnVal[ri][rj]= collisionMap[i][j];
			}
		}
		return returnVal;
	}

	public int getCollisionRowFor(Point2D position2D) {
		return (int)(position2D.getY()+mapHeight/2)/collisionMapDivisionHeight;
	}
	public int getCollisionColumnFor(Point2D position2D){
		return (int)(position2D.getX()+mapWidth/2)/collisionMapDivisionWidth;
	}
}
