package model.queue;

import model.MapStructure;
import model.TownHallLevel;
import model.map.Hex;
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
        for (Hex townHall : gameState.getEmpireState().getTownHallHexes()) {
            MapStructure building = townHall.getBuilding();
            if (building != null && building.isTownHall()) {
                building.setHP(building.getHP() + 50);
                building.setMaxHP(targetLevel.getMaxHP());
            }
        }
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
