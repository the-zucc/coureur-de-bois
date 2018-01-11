package collision;

import java.util.ArrayList;
import java.util.Vector;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Box;
import model.Model;

public class CollisionMatrix {
	
	private static int mapDivisionWidth=100;
	private static int mapDivisionHeight=100;
	private Group floor;
	private Vector<Vector<Double>> heightMatrix;
	
	//for optimizing the collision detection system
	private Vector<Vector<ArrayList<CollisionBox>>> mapDivisions;
	private ArrayList<CollisionBox> universalCollisionList;
	
	public static int getMapDivisionWidth() {
		return mapDivisionWidth;
	}
	public static int getMapDivisionHeight() {
		return mapDivisionHeight;
	}
	public int getNumberOfMapDivisionRows() {
		return mapDivisions.size();
	}
	public int getNumberOfMapDivisionColumns() {
		return mapDivisions.get(0).size();
	}
	
	public CollisionMatrix(int length, int height){
		//heightMatrix = new Vector<Vector<Double>>();
		floor = new Group();
		floor.getChildren().addAll(new Box(length, 1, height));
		int numberOfColumns = length / mapDivisionWidth;
		System.out.println(numberOfColumns);
		int numberOfRows = height / mapDivisionHeight;
		universalCollisionList = new ArrayList<CollisionBox>();
		mapDivisions = new Vector<Vector<ArrayList<CollisionBox>>>();
		heightMatrix = new Vector<Vector<Double>>(); 
		for(int i = 0; i < numberOfRows; i++) {
			heightMatrix.add(new Vector<Double>());
			mapDivisions.add(new Vector<ArrayList<CollisionBox>>());
			for(int j = 0; j < numberOfColumns; j++) {
				heightMatrix.get(i).add(100.0);
				mapDivisions.get(i).add(new ArrayList<CollisionBox>());
			}
		}
		
//		for(int i = 0; i < height; i++){
//			heightMatrix.add(new Vector<Double>());
//			for(int j = 0; j < length; j++){
//				heightMatrix.get(i).add(0.0);
//			}
//		}
	}
	public double getHeightAt(int row, int column){
		return heightMatrix.get(row).get(column);
	}
	public Group getFloor() {
		return floor;
	}
	public Bounds getFloorBounds() {
		return floor.getBoundsInLocal();
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
			System.out.println("could not find division to remove");
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
			System.out.println("could not find division to add");
		}
	}
	public Vector<ArrayList<CollisionBox>> getRow(int row){
		return mapDivisions.get(row);
	}
	public ArrayList<CollisionBox> getUniversalCollisionList(){
		return universalCollisionList;
	}
}
