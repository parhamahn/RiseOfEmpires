package model.queue;

import model.TownHallLevel;
import model.state.GameState;

public class UpgradeQueue extends Queue {

    private TownHallLevel targetLevel;


    public UpgradeQueue(TownHallLevel targetLevel, int turnCost, int startTurn){
        super(QUEUE_TYPE.UPGRADE, turnCost, startTurn);
        this.targetLevel = targetLevel;
    }


    public TownHallLevel getTargetLevel() {
        return targetLevel;
    }

    @Override
    public boolean execute(GameState gameState) {
        gameState.getEmpireState().setTownHallLevel(targetLevel);
        gameState.getEmpireState().getStorage().upgradeStorage(targetLevel.getMaxStorage());
        gameState.getEmpireState().getTownHallHex().getBuilding().setHP(targetLevel.getMaxHP());
        gameState.getEmpireState().getTownHallHex().getBuilding().setMaxHP(targetLevel.getMaxHP());
        return false;
    }
}
