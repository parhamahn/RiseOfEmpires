package controller.handler.building.townhall;

import controller.handler.BuildingHandler;
import model.TownHallLevel;
import model.queue.ResearchQueue;
import model.queue.Technology;
import model.queue.UpgradeQueue;
import model.state.GameState;
import model.unit.Unit;
import view.game.GamePanel;

public class ResearchHandler {

    private final GameState gameState;
    private final GamePanel gamePanel;

    public ResearchHandler(GameState gameState, GamePanel gamePanel) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
    }

    public void attachResearchListeners() {

        for(Technology technology:Technology.values()){

            gamePanel.getTownHallPanel().getResearchButton(technology).addActionListener(e->{

                if (!gameState.getEmpireState().canResearch(technology)) return;

                ResearchQueue researchQueue = new ResearchQueue(technology, technology.getTurnCost(), gameState.getWorldState().getTurnNumber());
                researchQueue.getTechnology().getCost().pay(gameState.getEmpireState().getStorage());
                gameState.getEmpireState().getQueues().add(researchQueue);
                gamePanel.getHudPanel().refresh();
                gamePanel.getTownHallPanel().refresh();

            });

        }
    }
}
