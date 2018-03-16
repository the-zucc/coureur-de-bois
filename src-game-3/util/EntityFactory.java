package util;

import entity.Entity;
import entity.living.human.Villager;
import entity.statics.village.Tipi;
import game.Map;
import javafx.geometry.Point3D;
import village.Village;

public class EntityFactory {
    public static Villager spawnVillagerFromVillage(Village v, Map m){
        Point3D position = PositionGenerator.getFloorPosition(PositionGenerator.generate2DPositionInRadius(v.get2DPosition(), v.getRadius()), m);
        Villager returnVal = new Villager(position, v);
        return returnVal;
    }
    public static Tipi buildTipiAroundVillage(Village v, Map m){
        Point3D position = PositionGenerator.getFloorPosition(PositionGenerator.generateTipiPositionInVillage(v), m);
        Tipi returnVal = new Tipi(position);
        return returnVal;
    }
}
