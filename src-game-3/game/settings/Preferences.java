package game.settings;

public class Preferences {
	private static float waterLevel = 0;
	private static double mapHeight = 20000;
	private static double mapWidth = 20000;
	private static int treeCount = 1000;


	public static float getWaterLevel() {
		return waterLevel;
	}
	public static void setWaterLevel(float waterLevel) {
		Preferences.waterLevel = waterLevel;
	}
	
	
	public static double getMapHeight() {
		return mapHeight;
	}
	public static void setMapHeight(double value) {
		Preferences.mapHeight = value;
	}
	
	public static double getMapWidth() {
		return mapWidth;
	}
	public static void setMapWidth(double value) {
		Preferences.mapWidth = value;
		
	}

    public static void setTreeCount(int treeCount) {
        Preferences.treeCount = treeCount;
    }

	public static int getTreeCount() {
		return treeCount;
	}
}
