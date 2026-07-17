package model.state;

import model.building.Building;
import model.building.TradingCenter;
import model.map.Hex;
import model.map.Map;
import model.product.Production;
import model.storage.Storage;

import java.util.Random;

public class GameState {
    private final EmpireState empireState;
    private final WorldState worldState;

    public GameState(Storage storage, Production production, Map map) {
        this.worldState = new WorldState(map);
        this.empireState = new EmpireState(storage, production);
        restartGame();
    }

    public void restartGame() {
        empireState.init(worldState.getMap());
    }

    public EmpireState getEmpireState() { return empireState; }
    public WorldState getWorldState() { return worldState; }

    public void randomTradingCenters(int n, Map map){
        Random rand = new Random();
        for(int i=0;i<n;i++){
            Hex x = map.getAllHexes().get(rand.nextInt(map.getAllHexes().size()));
            if(x.getBuilding()==null){
                Building y = new TradingCenter(x);
                x.setBuilding(y);
                empireState.getBuildings().add(y);
            }else{
                i-=1;
            }
        }
    }

    public void reset(){
        Map map = new Map(50, 100);
        empireState.getStorage().reset();
        empireState.getProduction().reset();
        empireState.getBuildings().clear();
        worldState.reset(map);
        empireState.init(map);
        randomTradingCenters(15, map);
    }

}
