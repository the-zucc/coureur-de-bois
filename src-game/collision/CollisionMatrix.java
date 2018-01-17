package collision;

import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
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
		
		int vertexheight = 10;
		int vertexwidth = 10;
		int cols = length/vertexheight;
		int rows = length/vertexwidth;
		
		floor = buildFloor(rows, cols, vertexheight, vertexwidth);
		//floor.getChildren().addAll(new Box(length, 1, height));
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
	
	private static Group buildFloor(int cols, int rows, int colwidth, int rowheight) {
		Group returnVal = new Group();
		double height = rowheight;
		double width = colwidth;
		float[][] yValues = new float[rows][cols];
		for(int z = 0; z < rows; z++) {
			for(int x = 0; x < cols; x++) {
				float y = (float)ThreadLocalRandom.current().nextDouble()*50;
				yValues[z][x]=y;
			}
		}
		for(int z = 0; z < rows-1; z++) {
			TriangleMesh mesh = new TriangleMesh();
			for(int x = 0; x < cols; x++) {
				mesh.getPoints().addAll((float)((-x*width)-Model.minCoordDebug), yValues[z][x], (float)(((-z)*height)-Model.minCoordDebug));
				mesh.getPoints().addAll((float)((-x*width)-Model.minCoordDebug), yValues[z+1][x], (float)(((-(z+1))*height)-Model.minCoordDebug));
			}
			mesh.getTexCoords().addAll(0,0);
			for(int i=2;i<cols*2;i+=2) {  //add each segment
		        //Vertices wound counter-clockwise which is the default front face of any Triange
		        //These triangles live on the frontside of the line facing the camera
		        mesh.getFaces().addAll(i,0,i-2,0,i+1,0); //add primary face
		        mesh.getFaces().addAll(i+1,0,i-2,0,i-1,0); //add secondary Width face
		        //Add the same faces but wind them clockwise so that the color looks correct when camera is rotated
		        //These triangles live on the backside of the line facing away from initial the camera
		        mesh.getFaces().addAll(i+1,0,i-2,0,i,0); //add primary face
		        mesh.getFaces().addAll(i-1,0,i-2,0,i+1,0); //add secondary Width face
		    }
			MeshView meshView = new MeshView(mesh);
			meshView.setDrawMode(DrawMode.FILL);
			
			PhongMaterial material = new PhongMaterial();
//			int choice = z%4;
//			switch(choice) {
//			case 0:
//				material.setDiffuseColor(Color.BROWN);
//				break;
//			case 1:
//				material.setDiffuseColor(Color.CORAL);
//				break;
//			case 2:
//				material.setDiffuseColor(Color.BLUEVIOLET);
//				break;
//			case 3:
//				material.setDiffuseColor(Color.CYAN);
//				break;
//			}
			material.setDiffuseColor(Color.GREEN);
			meshView.setMaterial(material);
			meshView.setTranslateX(0);
			meshView.setTranslateY(-25);
			meshView.setTranslateZ(0);
			returnVal.getChildren().add(meshView);
		}
		
		return returnVal;
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

