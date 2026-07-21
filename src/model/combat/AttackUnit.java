package model.combat;

import model.map.Hex;
import model.state.GameState;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.List;


public class AttackUnit {

    private GameState gameState;
    private final Hex attackerHex;
    private final Hex defenderHex;
    private List<Integer> attackerDices;
    private List<Integer> defenderDices;
    private int attackerPower;
    private int defenderPower;

    public AttackUnit(GameState gameState, Hex attackerHex, Hex defenderHex){
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
        return gameState.getWorldState().getMap().getDistance(attackerHex, defenderHex);
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
        for (Unit.UNIT_TYPE unitType : Unit.UNIT_TYPE.values()) {
            if (unitType != Unit.UNIT_TYPE.CATAPULT &&
                    attackerHex.getUnits().stream()
                            .anyMatch(unit -> unit.getUnitType() == unitType && unit.isCombatUnit())) {
                diceCount += 1;
            }
        }
        return diceCount;
    }

    private int getDefenderDiceCount(){
        int diceCount = 0;
        for (Unit.UNIT_TYPE unitType : Unit.UNIT_TYPE.values()) {
            if (unitType != Unit.UNIT_TYPE.CATAPULT &&
                    defenderHex.getUnits().stream()
                            .anyMatch(unit -> unit.getUnitType() == unitType && unit.isCombatUnit())) {
                diceCount += 1;
            }
        }
        return diceCount;
    }

    public void attack(){
        List<Unit> defenders = new ArrayList<>();
        for (Unit unit : defenderHex.getUnits()) {
            if (unit.isCombatUnit()) defenders.add(unit);
        }

        DamageHandler chain = new SwordsmanDamageHandler();
        chain.setNext(new ArcherDamageHandler())
             .setNext(new CavalryDamageHandler())
             .setNext(new CatapultDamageHandler());

        chain.handle(defenders, this.defenderPower, gameState);
    }

}
