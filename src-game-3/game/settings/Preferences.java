package game.settings;

public class Preferences {
	private static double waterLevel = 0;
	private static double mapHeight = 20000;
	private static double mapWidth = 20000;

	
	public static double getWaterLevel() {
		return waterLevel;
	}
	public static void setWaterLevel(double waterHeight) {
		Preferences.waterLevel = waterHeight;
	}
	
	
	public static double getMapHeight() {
		return mapHeight;
	}
	public static void setMapHeight(double mapHeight) {
		Preferences.mapHeight = mapHeight;
	}

	
	public static double getMapWidth() {
		return mapWidth;
	}
	public static void setMapWidth(double mapWidth) {
		Preferences.mapWidth = mapWidth;
	}
}
