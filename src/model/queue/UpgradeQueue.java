package model.queue;

import model.TownHallLevel;
import model.state.GameState;

import java.io.Serializable;

public class UpgradeQueue extends Queue implements Serializable {
private static final long serialVersionUID = 1L;

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
        gameState.getEmpireState().getTownHallHex().getBuilding().setHP(gameState.getEmpireState().getTownHallHex().getBuilding().getHP()+50);
        gameState.getEmpireState().getTownHallHex().getBuilding().setMaxHP(targetLevel.getMaxHP());
        return false;
    }

    @Override
    public void cancel(GameState gameState) {
    }

    @Override
    public String getDescription() {
        return "Upgrading to " + formatted(targetLevel);
    }
}
