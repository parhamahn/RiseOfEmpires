package controller;

import controller.handler.*;
import controller.handler.building.townhall.CookUnitHandler;
import controller.handler.building.townhall.ResearchHandler;
import controller.handler.building.townhall.UpgradeHandler;
import model.state.GameState;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;

public class GameController {
    private GamePanel gamePanel;
    private UnitHandler unitHandler;
    private BuildingHandler buildingHandler;

    public GameController(GamePanel gamePanel, JPanel cards, GameState gameState, SelectionContext selectionContext){
        this.gamePanel = gamePanel;
        CookUnitHandler cookUnitHandler = new CookUnitHandler();
        this.buildingHandler = new BuildingHandler(gameState, gamePanel, cookUnitHandler, selectionContext);
        this.unitHandler = new UnitHandler(gameState, gamePanel, selectionContext, buildingHandler);
        TurnHandler turnHandler = new TurnHandler(gameState, gamePanel, cookUnitHandler, buildingHandler);
        HUDHandler hudHandler = new HUDHandler(gamePanel, cards, turnHandler);
        HexHandler hexHandler = new HexHandler(gameState, gamePanel, unitHandler, buildingHandler, selectionContext);
        UpgradeHandler upgradeHandler = new UpgradeHandler(gameState, gamePanel, cookUnitHandler, buildingHandler);
        ResearchHandler researchHandler = new ResearchHandler(gameState, gamePanel);


        new CameraHandler(gamePanel).attachCameraListeners();

        hudHandler.attachHUDHandler();
        hexHandler.attachHexSelectionListener();
        upgradeHandler.attachTownHallPanelListeners();
        researchHandler.attachTownHallPanelListeners();
        gamePanel.getMapPanel().setOnUnitArrived(this::onArrive);

    }

    public void onArrive(){
        unitHandler.updateUnitListPanel();
        buildingHandler.updateTaskListPanel();
        unitHandler.discover();
        gamePanel.repaint();
    }

}
