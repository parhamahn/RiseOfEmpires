package model.queue;

import model.TownHallLevel;

public class UpgradeQueue extends Queue {

    private TownHallLevel targetLevel;


    public UpgradeQueue(TownHallLevel targetLevel, int turnCost, int startTurn){
        super(QUEUE_TYPE.UPGRADE, turnCost, startTurn);
        this.targetLevel = targetLevel;
    }


    public TownHallLevel getTargetLevel() {
        return targetLevel;
    }
}
