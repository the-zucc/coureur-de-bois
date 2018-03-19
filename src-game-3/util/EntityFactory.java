package util;

import entity.Entity;
import entity.living.human.Villager;
import entity.statics.village.Tipi;
import game.Map;
import javafx.geometry.Point3D;
import village.Village;

public class EntityFactory {
    public static Villager spawnVillagerFromVillage(Village v, Map map){
        Point3D position = PositionGenerator.getFloorPosition(PositionGenerator.generate2DPositionInRadius(v.get2DPosition(), v.getRadius()), map);
        Villager returnVal = new Villager(position, map, v);
        return returnVal;
    }
    public static Tipi buildTipiAroundVillage(Village v, Map map){
        Point3D position = PositionGenerator.getFloorPosition(PositionGenerator.generateTipiPositionInVillage(v), map);
        Tipi returnVal = new Tipi(position, map);
        return returnVal;
    }
}
