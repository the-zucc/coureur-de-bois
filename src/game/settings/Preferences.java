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


	private static float mapHeight = 800;
	public static float getMapHeight() {
		return mapHeight;
	}
	public static void setMapHeight(float value) {
		Preferences.mapHeight = value;
	}


	private static float mapWidth = 800;
	public static float getMapWidth() {
		return mapWidth;
	}
	public static void setMapWidth(float value) {
		Preferences.mapWidth = value;
	}


	private static float mapDetail = 4*GameLogic.getMeterLength();
	public static float getMapDetail() {
		return mapDetail;
	}
	public static void setMapDetail(float mapDetail) {
		Preferences.mapDetail = mapDetail*GameLogic.getMeterLength();
	}


	private static int treeCount = 150;
	public static int getTreeCount() {
		return treeCount;
	}
	public static void setTreeCount(int treeCount) {
		Preferences.treeCount = treeCount;
	}


	private static float villageRadius = 10*GameLogic.getMeterLength();
	public static float getVillageRadius(){
		return villageRadius;
	}
	public static void setVillageRadius(float villageRadius) {
		Preferences.villageRadius = villageRadius;
	}


	private static int villageCount = 1;
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
