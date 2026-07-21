package model.queue;

import model.state.GameState;

public class ResearchQueue extends Queue {

    private Technology technology;


    public ResearchQueue(Technology technology, int turnCost, int startTurn){
        super(QUEUE_TYPE.RESEARCH, turnCost, startTurn);
        this.technology = technology;
    }


    public Technology getTechnology() {
        return technology;
    }

    @Override
    public boolean execute(GameState gameState) {
        gameState.getEmpireState().getResearchedTechnologies().add(technology);
        if (technology == Technology.DEFENSIVE_PATTERN) {
            ProcessTechnology.processDefensivePattern(gameState);
            return true;
        }
        return false;
    }
}
