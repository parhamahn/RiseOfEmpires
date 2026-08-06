package model.map.generator;

import model.building.Building;
import model.building.other.TradingCenter;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;

import java.util.Random;

public class TradingCenterGenerator {
    public static int count = 15;

    public static void generateTradingCenters(GameState gameState){
        Map map = gameState.getWorldState().getMap();
        Random rand = gameState.getRng();
        for(int i=0;i<count;i++){
            Hex x = map.getAllHexes().get(rand.nextInt(map.getAllHexes().size()));
            if(x.getBuilding()==null){
                Building y = new TradingCenter(x);
                x.setBuilding(y);
                gameState.getWorldState().getTradingCenters().add((TradingCenter) y);
            }else{
                i-=1;
            }
        }
    }

}
