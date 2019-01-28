package game.settings;

import javafx.scene.SceneAntialiasing;

public class Settings {


    private static boolean antialiasing;
    private static double cameraDistance;
    private static double cameraAngle;

    public static boolean isAntialiasing() {
        return antialiasing;
    }

    public static void setAntialiasing(boolean antialiasing) {
        Settings.antialiasing = antialiasing;
    }
    
    public static SceneAntialiasing getAntialiasingValue(){
    	if(isAntialiasing())
    		return SceneAntialiasing.BALANCED;
    	return SceneAntialiasing.DISABLED;
    }
    
    public static double getCameraDistance() {
        return cameraDistance;
    }

    public static void setCameraDistance(double cameraDistance) {
        Settings.cameraDistance = cameraDistance;
    }

    public static double getCameraAngle() {
        return cameraAngle;
    }

    public static void setCameraAngle(double cameraAngle) {
        Settings.cameraAngle = cameraAngle;
    }
}
