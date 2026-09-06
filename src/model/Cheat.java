package model;

import model.map.Hex;
import model.map.Map;
import model.state.EmpireState;
import model.state.GameState;
import model.storage.Storage;
import model.unit.Unit;

import java.util.UUID;

public class Cheat {

    public static void greedIsGood(EmpireState empireState){
        Storage storage = empireState.getStorage();
        storage.setFoodQuantity(storage.getFoodQuantity()+500);
        storage.setWoodQuantity(storage.getWoodQuantity()+500);
        storage.setStoneQuantity(storage.getStoneQuantity()+500);
        storage.setIronQuantity(storage.getIronQuantity()+500);
    }

    public static void marco(GameState gameState, UUID sender){
        Map map = gameState.getWorldState().getMap();
        for(Hex hex:map.getAllHexes()){
            hex.getDiscoveredBy().add(sender);
        }
    }

    public static void redbull(EmpireState empireState){
        for(Unit unit:empireState.getUnits()){
            unit.setActionPoints(unit.getActionPointsMax());
        }
    }

}
