package model.combat;

import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.*;

import java.util.ArrayList;
import java.util.List;


public class Attack{

    private GameState gameState;
    private final Hex attackerHex;
    private final Hex defenderHex;
    private List<Integer> attackerDices;
    private List<Integer> defenderDices;
    private int attackerPower;
    private int defenderPower;

    public Attack(GameState gameState, Hex attackerHex, Hex defenderHex){
        this.attackerHex = attackerHex;
        this.defenderHex = defenderHex;
        this.gameState = gameState;
    }

    public Hex getAttackerHex() {return attackerHex;}
    public Hex getDefenderHex() {return defenderHex;}



    public void setDices(){
        this.attackerDices = getAttackerDices();
        this.defenderDices = getDefenderDices();
    }

    public void setPowers(){
        this.attackerPower = 0;
        this.defenderPower = 0;
        for(int i = 0; i < attackerDices.size(); i++){
            if(attackerDices.get(i) >= defenderDices.get(i)){
                this.attackerPower += 1;
            }else if(attackerDices.get(i) < defenderDices.get(i)){
                this.defenderPower += 1;
            }
        }
    }

    public int getDistance() {
        Map map = gameState.getWorldState().getMap();
        if (map.getNeighbourHexes(attackerHex).contains(defenderHex)) return 1;
        for (Hex n : map.getNeighbourHexes(attackerHex)) {
            if (map.getNeighbourHexes(n).contains(defenderHex)) return 2;
        }
        return -1;
    }

    private List<Integer> getAttackerDices(){
        List<Integer> dices =  new ArrayList<>();
        for(int i=1;i<=getAttackerDiceCount();i++){
            dices.add(Dice.getInstance().roll());
        }
        dices.sort(Integer::compareTo);
        return dices;
    }

    private List<Integer> getDefenderDices(){
        List<Integer> dices =  new ArrayList<>();
        for(int i=1;i<=getDefenderDiceCount();i++){
            dices.add(Dice.getInstance().roll());
        }
        dices.sort(Integer::compareTo);
        return dices;
    }

    private int getAttackerDiceCount(){
        int diceCount = 0;
        for(Unit.UNIT_TYPE unitType : Unit.UNIT_TYPE.values()){
            if(unitType!= Unit.UNIT_TYPE.CATAPULT && attackerHex.getUnits().stream().anyMatch(unit -> unitType.equals(unit) && unit.isCombatUnit())){
                diceCount += 1;
            }
        }
        return diceCount;
    }

    private int getDefenderDiceCount(){
        int diceCount = 0;
        for(Unit.UNIT_TYPE unitType : Unit.UNIT_TYPE.values()){
            if(unitType!= Unit.UNIT_TYPE.CATAPULT && defenderHex.getUnits().stream().anyMatch(unit -> unitType.equals(unit) && unit.isCombatUnit())){
                diceCount += 1;
            }
        }
        return diceCount;
    }

    public void attack(){
        List<Swordsman> attackerSwordmen = new ArrayList<>();
        List<Archer> attackerArchers = new ArrayList<>();
        List<Cavalry> attackerCavalry = new ArrayList<>();
        List<Catapult> attackerCatapults = new ArrayList<>();
        for (Unit unit: defenderHex.getUnits()) {
            if(unit instanceof Swordsman){attackerSwordmen.add((Swordsman) unit);}
            if(unit instanceof Archer){attackerArchers.add((Archer) unit);}
            if(unit instanceof Cavalry){attackerCavalry.add((Cavalry) unit);}
            if(unit instanceof Catapult){attackerCatapults.add((Catapult) unit);}
        }

        for(int i = 0; i < this.defenderPower; i++){
            if(!attackerSwordmen.isEmpty()){
                attackerSwordmen.get(0).setHealth(attackerSwordmen.get(0).getHealth()-1);
                if(attackerSwordmen.get(0).getHealth() == 0){
                    Swordsman dead = attackerSwordmen.remove(0);
                    gameState.getEmpireState().getUnits().remove(dead);
                    attackerHex.getUnits().remove(dead);
                }
            }else if(!attackerArchers.isEmpty()){
                attackerArchers.get(0).setHealth(attackerArchers.get(0).getHealth()-1);
                if(attackerArchers.get(0).getHealth() == 0){
                    Archer dead = attackerArchers.remove(0);
                    gameState.getEmpireState().getUnits().remove(dead);
                    attackerHex.getUnits().remove(dead);
                }
            }else if(!attackerCavalry.isEmpty()){
                attackerCavalry.get(0).setHealth(attackerCavalry.get(0).getHealth()-1);
                if(attackerCavalry.get(0).getHealth() == 0){
                    Cavalry dead = attackerCavalry.remove(0);
                    gameState.getEmpireState().getUnits().remove(dead);
                    attackerHex.getUnits().remove(dead);
                }
            }else if(!attackerCatapults.isEmpty()){
                attackerCatapults.get(0).setHealth(attackerCatapults.get(0).getHealth()-1);
                if(attackerCatapults.get(0).getHealth() == 0){
                    Catapult dead = attackerCatapults.remove(0);
                    gameState.getEmpireState().getUnits().remove(dead);
                    attackerHex.getUnits().remove(dead);
                }
            }
        }

    }

}
