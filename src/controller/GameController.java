package controller;

import controller.handler.*;
import model.state.GameState;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.*;

public class GameController {
    private JPanel cards;
    private GamePanel gamePanel;
    private UnitHandler unitHandler;
    private BuildingHandler buildingHandler;

    public GameController(GamePanel gamePanel, JPanel cards, GameState gameState, SelectionContext selectionContext){
        this.gamePanel = gamePanel;
        this.cards = cards;
        CookUnitHandler cookUnitHandler = new CookUnitHandler();
        this.buildingHandler = new BuildingHandler(gameState, gamePanel, cookUnitHandler, selectionContext);
        this.unitHandler = new UnitHandler(gameState, gamePanel, selectionContext, buildingHandler);
        TurnHandler turnHandler = new TurnHandler(gameState, gamePanel, cookUnitHandler, buildingHandler);
        HexHandler hexHandler = new HexHandler(gameState, gamePanel, unitHandler, buildingHandler, selectionContext);

        gamePanel.getHudPanel().getMenuButton().addActionListener(e -> menuAction());
        gamePanel.getHudPanel().getEndTurnButton().addActionListener(e -> turnHandler.endTurnAction());
        new CameraHandler(gamePanel).attachCameraListeners();
        hexHandler.attachHexSelectionListener();
        buildingHandler.attachTownHallPanelListeners();
        gamePanel.getMapPanel().setOnUnitArrived(this::onArrive);
    }

    private void menuAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }

    public void onArrive(){
        unitHandler.updateUnitListPanel();
        buildingHandler.updateTaskListPanel();
        unitHandler.discover();
        gamePanel.repaint();
    }

}
