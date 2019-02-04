package game;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

import app.App;
import characteristic.ComponentOwner;
import characteristic.MessageReceiver;
import characteristic.Messenger;
import characteristic.Updateable;
import characteristic.positionnable.Collideable;
import characteristic.positionnable.Positionnable;
import characteristic.positionnable.Positionnable2D;
import entity.Entity;
import entity.MovingCollidingEntity;
import entity.VisibleEntity;
import entity.living.animal.Beaver;
import entity.living.animal.Fox;
import entity.living.human.Player;
import entity.statics.tree.PineTree;
import entity.statics.village.Cafe;
import entity.statics.village.House;
import entity.wearable.LongSword;
import entity.wearable.StandardSword;
import entity.wearable.WoodCuttersAxe;
import entity.statics.tree.Tree;
import game.settings.Preferences;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import perlin.PerlinNoise;
import ui.gamescene.GameCamera;
import util.MessageCallback;
import util.PositionGenerator;
import village.Village;
import visual.Component;

public class Map implements ComponentOwner, Updateable, MessageReceiver{
	
	private static Map mainMap;
	
	public static Map getMainMap() {
		if (mainMap == null) {
			try {
				mainMap = new Map(Preferences.getMapWidth(),
						Preferences.getMapHeight(),
						Preferences.getMapDetail(),
						Preferences.getMapDetail(),
						Preferences.getWaterLevel(),
						Preferences.getTreeCount(),
						Preferences.getVillageCount(),
						Preferences.getVillageRadius(),
						Preferences.getVillageTipiCount(),
						Preferences.getVillageVillagerCount(),
						150);
			}catch(Exception e){
				return getMainMap();
			}
		}
		return mainMap;
	}

	private Player currentPlayer;
	public Player getCurrentPlayer(){
		return currentPlayer;
	}
	public void setCurrentPlayer(Player p){
		currentPlayer = p;
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

	@Override
	public float distanceFrom(Positionnable p) {
		return (float)p.getPosition().distance(this.getPosition());
	}

	protected Component component;
	@Override
	public Component getComponent() {
		return component;
	}
	@Override
	public Component buildComponent() {
		Component returnVal = new Component("id_map");
		returnVal.setPosition(position);
		Component floorMesh = buildFloorMesh();
		returnVal.addChildComponent(floorMesh);
		returnVal.addChildComponent(buildWaterMesh((float)waterLevel));
		floorMesh.setOnMouseClicked((e)->{
			if(e.getButton() == MouseButton.SECONDARY) {
				messenger.send("right_clicked", PositionGenerator.convert2D(e.getPickResult().getIntersectedPoint()));
			}
		});
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


	protected GameCamera gameCamera;
	protected float mapWidth;
	protected float mapHeight;
	public float getMapWidth(){
		return mapWidth;
	}
	public float getMapHeight(){
		return mapHeight;
	}
	private float vertexSeparationWidth;
	private float vertexSeparationHeight;
	
	private float[][] heightMatrix;
	private Point3D[][] floorVertices;
	private float waterLevel;

	protected ArrayList<Collideable> collideables;
	protected ArrayList<Updateable> updateables;
	protected ArrayList<Entity> entities;
	protected ArrayList<ComponentOwner> componentOwners;
	private ArrayList<Village> villages;
	
	protected Messenger messenger;
	public Messenger getMessenger() {
		return messenger;
	}
	private ArrayList<Tree> trees = new ArrayList<Tree>();
	public ArrayList<Tree> getTrees(){
		return trees;
	}

	private Map currentMap;
	public Map getCurrentMap() {
		return currentMap;
	}

	public Map() throws Exception{

	}
	public Map(float mapWidth,
			float mapHeight,
			float vertexSeparationWidth,
			float vertexSeparationHeight,
			float waterLevel,
			int treeCount,
			int villageCount,
			float villageRadius,
			int tipiCount,
			int villagerCount,
			int sheepCount) throws Exception
	{
		/**
		 * current Map, where the player is.
		 */
		this.currentMap = this;
		/**
		 * messenger functionality
		 */
		messenger = createMessenger();
		listenTo(messenger);
		/**
		 * collisions
		 */
		collideables = new ArrayList<Collideable>();
		collisionCols = (int)mapWidth/collisionMapDivisionWidth;
		collisionRows = (int)mapHeight/collisionMapDivisionHeight;
		collisionMap = new ArrayList[collisionRows][collisionCols];
		for(int i = 0; i < collisionRows; i++){
			for(int j = 0; j < collisionCols; j++){
				collisionMap[i][j] = new ArrayList<Collideable>();
			}
		}
		/**
		 * updates
		 */
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

		currentPlayer = new Player(PositionGenerator.generateRandom3DPositionOnFloor(this), this, messenger);
		addEntity(currentPlayer);

		for (int i = 0; i < treeCount; i++) {
			Point3D pos = PositionGenerator.generateRandom3DPositionOnFloor(this);
			Tree tree;
			float val = (float)Math.random();
			if(val > 0.5){
				tree = new PineTree(pos, this, messenger);
			}
			else{
				tree = new Tree(pos,this, messenger);
			}
			addEntity(tree);
		}
		for (int i = 0; i < tipiCount; i++) {
			Point3D pos = PositionGenerator.generateRandom3DPositionOnFloor(this);
			addEntity(new Cafe(pos, this, messenger));
		}
		for(int i= 0; i < villageCount; i++){
			Point2D villagePos = PositionGenerator.generate2DPositionNotInVillages(this, villages);

			Village v = new Village(villagePos, tipiCount, 40*GameLogic.getMeterLength(),villagerCount, this);
			villages.add(v);
			v.addEntitiesToMap(this);
		}
		for(int i = 0; i < sheepCount; i++){
			if(Math.random() > 0.5) {
				addEntity(new Beaver(PositionGenerator.generateRandom3DPositionOnFloor(this), this, messenger));				
			}else {
				addEntity(new Fox(PositionGenerator.generateRandom3DPositionOnFloor(this), this, messenger));
			}
			
		}
		accept("dead", (params)->{
			removeEntity((Entity)params[0]);
		});
		for(int i = 0; i < sheepCount; i++) {
			Point3D swordPos = PositionGenerator.generateRandom3DPositionOnFloor(this);
			float val = (float)Math.random();
			if(val > 0.95) {
				addEntity(new StandardSword(swordPos, this, messenger));
			}else if(val > 0.9) {
				addEntity(new LongSword(swordPos, this, messenger));
			}else if(val > 0.85) {
				addEntity(new WoodCuttersAxe(swordPos, this, messenger));
			}
		}
        
        accept("drop", (params)->{
        	addEntity((Entity)params[0]);
        });
        accept("remove", (params)->{
        	removeEntity((Entity)params[0]);
        });
        accept("pause_enter_house", (params)->{
        	if(params[0] instanceof House){
        		setCurrentMap(((House)params[0]).getHouseMap());
			}
        	else{
				System.out.println(params[0].getClass().getName()+" "+params[0]+" is not a map.");
			}
		});
	}

	private void setCurrentMap(Map map) {
		//TODO fix this asap
		this.currentMap = map;
		Component c = getComponent();

		Group parent = (Group)c.getParent();
		if(parent != null){
			parent.getChildren().remove(c);
			try{
				parent.getChildren().add(map.getComponent());
			}catch(Exception e){

			}
			App.getUserInterface().getGameScreen().setFill(Color.BLACK);
			currentPlayer.setMap(map);
			this.removeEntity(currentPlayer);
			map.addEntity(currentPlayer);
			map.setCurrentPlayer(currentPlayer);
		}
	}

	protected Messenger createMessenger() {
		return new Messenger() {
			private ArrayList<MessageReceiver> receivers = new ArrayList<MessageReceiver>();
			@Override
			public void notifyReceivers(String message) {

			    if(getListeners().containsKey(message)) {
			    	for(MessageReceiver m: getListeners().get(message)) {
			    		m.receiveMessage(message);
			    	}
            	}
			}

			@Override
			public void notifyReceivers(String message, Object... params) {
				if(getListeners().containsKey(message)) {
			    	for(MessageReceiver m: getListeners().get(message)) {
			    		m.receiveMessage(message, params);
			    	}
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
				for(String key:getListeners().keySet()) {
					ArrayList<MessageReceiver> tmp = getListeners().get(key);
					if(tmp.contains(o)) {
						tmp.remove(o);
					}
				}
			}

			@Override
			public Hashtable<String, ArrayList<MessageReceiver>> getListeners() {
				return listeners;
			}
			private Hashtable<String, ArrayList<MessageReceiver>> listeners = new Hashtable<String, ArrayList<MessageReceiver>>(); 
			@Override
			public void setupListener(String message, MessageReceiver receiver) {
				if(!getListeners().containsKey(message)) {
					listeners.put(message, new ArrayList<MessageReceiver>());
				}
				listeners.get(message).add(receiver);
			}
		};
	}

	public float getHeightAt(Point2D arg0){
		try {
			float rowHeight = vertexSeparationHeight;
			float rowWidth = vertexSeparationWidth;
			
			
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
			
			float arg0p0x = (float)(arg0.getX()-p0.getX());

			float arg0p0z = (float)(arg0.getY()-p0.getZ());
			
			float returnVal = (float)(((normal.getX() * (arg0p0x) + normal.getZ() * (arg0p0z)/* - letD*/)/-normal.getY()) + p0.getY());
			
			if(returnVal > waterLevel)
				returnVal = waterLevel;
			
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
		for(float z  = mapHeight/2; z > -mapHeight/2; z-= vertexSeparationHeight, zi++){
			xi=0;
			for(float x = -mapWidth/2; x < mapWidth/2; x += vertexSeparationWidth, xi++){
				p1 = new Point3D(x,heightMatrix[zi][xi],z);
				floorVertices[zi][xi] = p1;
				mesh.getPoints().addAll((float)p1.getX(), (float)p1.getY(), (float)p1.getZ());
				if(zi<rows-1 && xi < cols-1){
					int idx = xi+(zi*cols);
					mesh.getFaces().addAll(idx+1,0,idx,0,idx+cols,0);
					mesh.getFaces().addAll(idx+cols,0,idx+cols+1,0,idx+1,0);
				}
			}
		}
		mesh.getTexCoords().addAll(0,0);

		MeshView floorMeshView = new MeshView(mesh);
		floorMeshView.setDrawMode(DrawMode.FILL);
		floorMeshView.setMaterial(new PhongMaterial(Color.WHITE));
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

		mesh.getFaces().addAll(0,0,1,0,2,0);
		mesh.getFaces().addAll(3,0,2,0,1,0);

		mesh.getTexCoords().addAll(0,0);

        MeshView meshView = new MeshView(mesh);
        meshView.setMaterial(new PhongMaterial(new Color(0,0.1,0.2,0.7)));
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
				float y = perlin.smoothNoise(x*multiplicator, z*multiplicator, 32, 24)*(float)(800*GameLogic.getMeterLength());
				returnVal[z][x]=y;
			}
		}
		return returnVal;
	}
	
	public ArrayList<ComponentOwner> getComponentOwners(){
		return componentOwners;
	}

	private int updateableCounter = 0;
	boolean isUpdating = false;
	Updateable currentUpdateable = null;
	@Override
	public void update(float secondsPassed) {
		isUpdating = true;
		/*MessageReceiver m = messenger.getReceivers().get(0);
		for(int i = 0; i < messenger.getReceivers().size(); i++) {
			if(!messenger.getReceivers().contains(m)) {
				i--;
			}
			m = messenger.getReceivers().get(i);
			m.processCallbackQueue();
		}*/
		updateUpdateables(secondsPassed);
		processCallbackQueue();
		isUpdating = false;
	}
	private void updateUpdateables(float secondsPassed) {
		try {
			for (Updateable u:updateables) {
				u.update(secondsPassed);
			}
		}catch(ConcurrentModificationException e) {
			e.printStackTrace();
			updateUpdateables(secondsPassed);
		}
	}
	@Override
	public boolean shouldUpdate() {
		return true;
	}

    public void addEntity(Entity e){
		if(e instanceof VisibleEntity){
			((VisibleEntity) e).setMap(this);
		}
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
			Platform.runLater(()->{getComponent().addChildComponent(((ComponentOwner)e).getComponent());});
		}
		if(e instanceof Tree) {
			trees.add((Tree)e);
		}
		entities.add(e);
	}
    
    private void removeEntity(Entity e) {
    	if(e instanceof Updateable){
			updateables.remove(((Updateable)e));
		}
		if(e instanceof Collideable){
			collideables.remove((Collideable)e);
			
			int row = ((Collideable)e).getCollisionMapRow();
			int col = ((Collideable)e).getCollisionMapColumn();
			try{
				collisionMap[row][col].remove((Collideable)e);
			}catch(ArrayIndexOutOfBoundsException aioobe){
				
			}
		}
		if(e instanceof ComponentOwner) {
			componentOwners.remove((ComponentOwner)e);
			Platform.runLater(()->{
				getComponent().getChildren().remove(((ComponentOwner)e).getComponent());
			});
		}
		if(e instanceof MessageReceiver) {
			if(messenger.getReceivers().contains(e)) {
				messenger.removeReceiver((MessageReceiver)e);
			}
		}
		if(e instanceof Tree) {
			trees.remove(e);
		}
		if(isUpdating){
			if(e instanceof Updateable) {
				if(e == currentUpdateable) {
					updateableCounter--;
					System.out.println(updateableCounter);
				}
			}
		}
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
	public float distance2DFrom(Positionnable2D arg0) {
		return (float)Point2D.ZERO.distance(arg0.get2DPosition());
	}

	public float getWaterLevel() {
		return waterLevel;
	}

	public void setGameCamera(GameCamera gc){
		gameCamera = gc;
		updateables.add(gc);
	}
	@Override
	public void onHover(MouseEvent me) {
		
	}

	@Override
	public void onUnHover(MouseEvent me) {
		
	}
	/**
	 * COLLISIONS
	 */
	//static variables
	protected static int collisionMapDivisionWidth=(int)(5*GameLogic.getMeterLength());
	protected static int collisionMapDivisionHeight=collisionMapDivisionWidth;
	protected int collisionCols;
	protected int collisionRows;
	
	public static int getCollisionMapDivisionWidth(){
		return collisionMapDivisionWidth;
	}
	public static int getCollisionMapDivisionHeight(){
		return collisionMapDivisionHeight;
	}
	protected ArrayList<Collideable>[][] collisionMap;
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

	@Override
	public void onClick(MouseEvent me) {
		
	}
	
	/**
	 * For messenger
	 */
	/**
	 * Section pour Messenger
	**/
	private ArrayList<Messenger> messengers = new ArrayList<Messenger>();
	@Override
	public ArrayList<Messenger> getMessengers(){
		return messengers;
	}

	@Override
	public void processCallbackQueue(){

		try{
			ArrayList<String> receivedMessages = getReceivedMessages();
			ArrayList<Object[]> receivedParams = getReceivedParams();
			while(!receivedMessages.isEmpty()) {
				getAccepts().get(receivedMessages.get(0)).run(receivedParams.get(0));
				receivedMessages.remove(0);
				receivedParams.remove(0);
			}
		}catch(ConcurrentModificationException cme){
			processCallbackQueue();
			return;
		}
	}
	@Override
	public void receiveMessage(String message, Object... params){
		getReceivedMessages().add(message);
		getReceivedParams().add(params);
	}

	@Override
	public void receiveMessage(String message){
		getReceivedMessages().add(message);
		getReceivedParams().add(null);
	}

	@Override
	public void accept(String message, MessageCallback callback){
		getAccepts().put(message, callback);
		messenger.setupListener(message, this);
	}
	
	Hashtable<String, MessageCallback> accepts = new Hashtable<String, MessageCallback>();
	@Override
	public Hashtable<String, MessageCallback> getAccepts(){
		return accepts;
	}
	
	@Override
	public void listenTo(Messenger messenger){
		getMessengers().add(messenger);
		messenger.addReceiver(this);
	}
	
	ArrayList<String>  receivedMessages = new ArrayList<String>();
	@Override
	public ArrayList<String> getReceivedMessages(){
		return receivedMessages;
	}
	
	ArrayList<Object[]> receivedParams = new ArrayList<Object[]>();
	@Override
	public ArrayList<Object[]> getReceivedParams(){
		return receivedParams;
	}
	
	public Cylinder createConnection(Point3D origin, Point3D target, PhongMaterial material) {
	    Point3D yAxis = new Point3D(0, 1, 0);
	    Point3D diff = target.subtract(origin);
	    float height = (float)diff.magnitude();

	    Point3D mid = target.midpoint(origin);
	    Translate moveToMidpoint = new Translate(mid.getX(), mid.getY(), mid.getZ());

	    Point3D axisOfRotation = diff.crossProduct(yAxis);
	    float angle = (float)Math.acos(diff.normalize().dotProduct(yAxis));
	    Rotate rotateAroundCenter = new Rotate(-Math.toDegrees(angle), axisOfRotation);

	    Cylinder line = new Cylinder(1, height);

	    line.getTransforms().addAll(moveToMidpoint, rotateAroundCenter);
	    line.setMaterial(material);

	    getComponent().getChildren().addAll(line);
	    return line;
	}

}
