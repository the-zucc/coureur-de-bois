package collision;

import java.util.ArrayList;
import java.util.Vector;

import app.GameScene;
import app.Model;
import entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseEvent;

public class CollisionGrid {
	
	//for singleton implementation
	//not needed for now
	//TODO delete this if not needed
	private static CollisionGrid instance;
	public static CollisionGrid getInstance() {
		if(instance == null)
			instance = new CollisionGrid(2000, 2000, 20, 20);
		return instance;
	}
	public static CollisionGrid newInstance(double mapWidth, double mapHeight, int columnCount, int rowCount) {
		instance = new CollisionGrid(mapWidth, mapHeight, columnCount, rowCount);
		return instance;
	}
	//static variables
	private static int mapDivisionWidth=100;
	private static int mapDivisionHeight=100;
	
	//instance variables
	private Point3D[][][] floorVertices;
	//for optimizing the collision detection system
	private Vector<Vector<ArrayList<CollisionBox>>> mapDivisions;
	private ArrayList<CollisionBox> universalCollisionList;
	private ArrayList<CollisionBox> collisionBoxes;
	
	public CollisionGrid(double mapWidth, double mapHeight, int columnCount, int rowCount) {
		//build the collision grid like in the old code
		collisionBoxes = new ArrayList<CollisionBox>();
		universalCollisionList = new ArrayList<CollisionBox>();
		mapDivisions = new Vector<Vector<ArrayList<CollisionBox>>>();
		//heightMatrix = new Vector<Vector<Double>>();
		
		for(int i = 0; i < rowCount; i++) {
			//heightMatrix.add(new Vector<Double>());
			mapDivisions.add(new Vector<ArrayList<CollisionBox>>());
			for(int j = 0; j < columnCount; j++) {
				//heightMatrix.get(i).add(100.0);
				mapDivisions.get(i).add(new ArrayList<CollisionBox>());
			}
		}
	}
	public void initFloorVertices(){
		floorVertices = GameScene.getHeightMatrix();
	}
	public void update() {
		for(Entity e:Model.getInstance().getGameElements()) {
			if(!collisionBoxes.contains(e.getCollisionBox())) {
				CollisionBox cb = e.getCollisionBox();
				collisionBoxes.add(e.getCollisionBox());
			}
		}
		for(CollisionBox cb:collisionBoxes) {
			Model model = Model.getInstance();
			Entity e = model.getEntity(cb.getId()); 
			if(e == null) {
				collisionBoxes.remove(cb.getId());
				removeFromDivision(cb.getMapDivisionRow(), cb.getMapDivisionColumn(), cb);
			}
			//else
			//	cb.setPosition(e.getPosition());
		}
	}
	
	public static int getMapDivisionWidth() {
		return mapDivisionWidth;
	}
	public static int getMapDivisionHeight() {
		return mapDivisionHeight;
	}
	/**
	 * removes the specified {@link CollisionBox} from the desired map division
	 * @param row the row of the division from which to remove the element 
	 * @param column the column of the division from which to remove the element
	 * @param ge the element to remove
	 */
	public void removeFromDivision(int row, int column, CollisionBox cb) {
		try{
			mapDivisions.get(row).get(column).remove(cb);
		}catch(ArrayIndexOutOfBoundsException aioobe){
			//System.out.println("could not find division to remove");
		}
	}
	/**
	 * add the specified {@link CollisionBox} to the desired map division.
	 * @param row the row of the division into which to add the element
	 * @param column the column of the division into which to add the element
	 * @param ge the element to add
	 */
	public void addToDivision(int row, int column, CollisionBox cb) {
		try{
			mapDivisions.get(row).get(column).add(cb);
		}catch(ArrayIndexOutOfBoundsException aioobe){
			//System.out.println("could not find division to add");
		}
	}
	public Vector<ArrayList<CollisionBox>> getRow(int row){
		return mapDivisions.get(row);
	}
	public ArrayList<CollisionBox> getUniversalCollisionList(){
		return universalCollisionList;
	}
	public int getNumberOfMapDivisionRows(){
		return mapDivisions.size();

	}
	public int getNumberOfMapDivisionColumns(){
		return mapDivisions.get(0).size();
	}
	public void addCollisionBox(CollisionBox cb) {
		this.collisionBoxes.add(cb);
		if(cb.tooBigForCollisionDetectionSystem)
			universalCollisionList.add(cb);
	}
	public double getHeightAt(Point3D arg0){
		Point3D[] currentTriangle = floorVertices[1][1];
		Point3D p0 = currentTriangle[0];
		Point3D vect1 = p0.subtract(currentTriangle[1]);
		Point3D vect2 = p0.subtract(currentTriangle[2]);
		Point3D normal = vect1.crossProduct(vect2).normalize();
		
		double returnVal = (normal.getX()*(arg0.getX()-p0.getX())+normal.getZ()*(arg0.getZ()-p0.getZ()))/normal.getY() + p0.getY();
		
		return returnVal;
	}
}
