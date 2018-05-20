package game.settings;

import game.GameLogic;

public class Preferences {

	private static float waterLevel = 0;
	public static float getWaterLevel() {
		return waterLevel;
	}
	public static void setWaterLevel(float waterLevel) {
		Preferences.waterLevel = waterLevel;
	}


	private static double mapHeight = 8000;
	public static double getMapHeight() {
		return mapHeight;
	}
	public static void setMapHeight(double value) {
		Preferences.mapHeight = value;
	}


	private static double mapWidth = 8000;
	public static double getMapWidth() {
		return mapWidth;
	}
	public static void setMapWidth(double value) {
		Preferences.mapWidth = value;
	}


	private static double mapDetail = 16*GameLogic.getMeterLength();
	public static double getMapDetail() {
		return mapDetail;
	}
	public static void setMapDetail(double mapDetail) {
		Preferences.mapDetail = mapDetail*GameLogic.getMeterLength();
	}


	private static int treeCount = 3000;
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


	private static int villageCount = 5;
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
