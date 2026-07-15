package model;

import model.state.GameState;

public class Season {

    public enum SeasonType {SPRING, SUMMER, FALL, WINTER}


    public static void processSeason(GameState gameState) {

        int x = gameState.getWorldState().getTurnNumber() % 40;

        if(x>=1 && x<=10){
            gameState.getEmpireState().setSeason(SeasonType.SPRING);
        }else if(x>=11 && x<=20){
            gameState.getEmpireState().setSeason(SeasonType.SUMMER);
        }else if(x>=21 && x<=30){
            gameState.getEmpireState().setSeason(SeasonType.FALL);
        }else if((x>=31 && x<=40) || x==0){
            gameState.getEmpireState().setSeason(SeasonType.WINTER);
        }

    }

}
