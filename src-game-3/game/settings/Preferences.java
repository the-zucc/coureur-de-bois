package game.settings;

public class Preferences {
	private static float waterLevel = 0;
	private static double mapHeight = 150000;
	private static double mapWidth = 150000;

	public static double getMapDetail() {
		return mapDetail;
	}

	public static void setMapDetail(double mapDetail) {
		Preferences.mapDetail = mapDetail;
	}

	private static double mapDetail = 200;
	private static int treeCount = 1000;

	private static double villageRadius = 500;
	private static int villageCount = 4;
	private static int villageTipiCount = 6;
    private static int villageVillagerCount = 10;

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

    public static void setVillageRadius(double villageRadius) {
        Preferences.villageRadius = villageRadius;
    }

    public static double getVillageRadius(){
		return villageRadius;
	}
    public static void setVillageCount(int villageCount) {
        Preferences.villageCount = villageCount;
    }

    public static void setVillageVillagerCount(int villageVillagerCount){
	    Preferences.villageVillagerCount = villageVillagerCount;
    }
    public static int getVillageVillagerCount(){
	    return villageVillagerCount;
    }

    public static void setVillageTipiCount(int villageTipiCount) {
        Preferences.villageTipiCount = villageTipiCount;
    }
    public static int getVillageTipiCount() {
        return villageTipiCount;
    }
}
