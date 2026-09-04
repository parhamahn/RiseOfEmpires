package controller;

import controller.handler.*;
import controller.handler.building.bazaar.BazaarHandler;
import controller.handler.building.townhall.CookUnitHandler;
import controller.handler.building.townhall.TownHallHandler;
import controller.handler.building.tradingcenter.TradingCenterHandler;
import controller.handler.building.tribe.TribeHandler;
import controller.handler.combat.CombatHandler;
import model.map.Hex;
import model.state.GameState;
import view.game.GamePanel;

import javax.swing.*;

public class GameController {
    private GamePanel gamePanel;
    private UnitHandler unitHandler;
    private BuildingHandler buildingHandler;

    public GameController(GamePanel gamePanel, JPanel cards, GameState gameState, SelectionContext selectionContext){
        this.gamePanel = gamePanel;
        CookUnitHandler cookUnitHandler = new CookUnitHandler();
        DisasterHandler disasterHandler = new DisasterHandler(gameState, gamePanel);
        this.buildingHandler = new BuildingHandler(gameState, gamePanel, cookUnitHandler, selectionContext);
        this.unitHandler = new UnitHandler(gameState, gamePanel, selectionContext, buildingHandler);
        TurnHandler turnHandler = new TurnHandler(gameState, gamePanel, cookUnitHandler, buildingHandler, disasterHandler);
        BazaarHandler bazaarHandler = new BazaarHandler(gamePanel, gameState);
        TradingCenterHandler tradingCenterHandler = new TradingCenterHandler(gamePanel, gameState);
        TribeHandler tribeHandler = new TribeHandler(gameState, gamePanel);
        HUDHandler hudHandler = new HUDHandler(gamePanel, cards, turnHandler);
        CombatHandler combatHandler = new CombatHandler(gameState, gamePanel, selectionContext);
        HexHandler hexHandler = new HexHandler(gameState, gamePanel, unitHandler, buildingHandler, selectionContext, bazaarHandler, tradingCenterHandler, tribeHandler, combatHandler);
        TownHallHandler townHallHandler = new TownHallHandler(gameState, gamePanel, buildingHandler);
        ChatHandler chatHandler = new ChatHandler(gameState, gamePanel);

        new CameraHandler(gamePanel).attachCameraListeners();

        hudHandler.attachHUDHandler();
        hexHandler.attachHexSelectionListener();
        townHallHandler.attachHandlers();
        bazaarHandler.attachHandlers();
        tradingCenterHandler.attachHandlers();
        tribeHandler.attachHandlers();
        gamePanel.getMapPanel().setOnUnitArrived(this::onArrive);
        combatHandler.attachHandlers();
        chatHandler.attachHandlers();

    }

    public void onArrive(){
        unitHandler.updateUnitListPanel();
        buildingHandler.updateTaskListPanel();
        unitHandler.discover();
        gamePanel.repaint();
    }

}
