package app;

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
	
	public CollisionGrid(double mapWidth, double mapHeight, int columnCount, int rowCount) {
		//build the collision grid like in the old code
	}
}
