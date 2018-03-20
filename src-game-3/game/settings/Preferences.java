package game.settings;

public class Preferences {

	private static float waterLevel = 0;
	public static float getWaterLevel() {
		return waterLevel;
	}
	public static void setWaterLevel(float waterLevel) {
		Preferences.waterLevel = waterLevel;
	}


	private static double mapHeight = 150000;
	public static double getMapHeight() {
		return mapHeight;
	}
	public static void setMapHeight(double value) {
		Preferences.mapHeight = value;
	}


	private static double mapWidth = 150000;
	public static double getMapWidth() {
		return mapWidth;
	}
	public static void setMapWidth(double value) {
		Preferences.mapWidth = value;
	}


	private static double mapDetail = 400;
	public static double getMapDetail() {
		return mapDetail;
	}
	public static void setMapDetail(double mapDetail) {
		Preferences.mapDetail = mapDetail;
	}


	private static int treeCount = 2000;
	public static int getTreeCount() {
		return treeCount;
	}
	public static void setTreeCount(int treeCount) {
		Preferences.treeCount = treeCount;
	}


	private static double villageRadius = 500;
	public static double getVillageRadius(){
		return villageRadius;
	}
	public static void setVillageRadius(double villageRadius) {
		Preferences.villageRadius = villageRadius;
	}


	private static int villageCount = 10;
	public static void setVillageCount(int villageCount) {
		Preferences.villageCount = villageCount;
	}
	public static int getVillageCount(){
		return villageCount;
	}


	private static int villageTipiCount = 6;
	public static int getVillageTipiCount() {
		return villageTipiCount;
	}
	public static void setVillageTipiCount(int villageTipiCount) {
		Preferences.villageTipiCount = villageTipiCount;
	}


	private static int villageVillagerCount = 10;
	public static int getVillageVillagerCount(){
		return villageVillagerCount;
	}
	public static void setVillageVillagerCount(int villageVillagerCount){
		Preferences.villageVillagerCount = villageVillagerCount;
	}

}
