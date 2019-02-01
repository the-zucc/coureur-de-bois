package util;

import entity.living.human.Villager;
import entity.statics.village.House;
import game.Map;
import javafx.geometry.Point3D;
import village.Village;

public class EntityFactory {
    public static Villager spawnVillagerFromVillage(Village v, Map map){
        Point3D position = PositionGenerator.getFloorPosition(PositionGenerator.generate2DPositionInRadius(v.get2DPosition(), v.getRadius()), map);
        Villager returnVal = new Villager(position, map, map.getMessenger(), v);
        return returnVal;
    }
    public static House buildTipiAroundVillage(Village v, Map map){
        Point3D position = PositionGenerator.getFloorPosition(PositionGenerator.generateTipiPositionInVillage(v), map);
        House returnVal = new House(position, map, map.getMessenger());
        return returnVal;
    }
}
