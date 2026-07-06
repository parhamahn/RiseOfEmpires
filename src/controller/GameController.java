package controller;

import controller.handler.*;
import model.GameState;
import view.GamePanel;

import javax.swing.*;
import java.awt.*;

public class GameController {
    private JPanel cards;
    private UnitHandler unitHandler;
    private BuildingHandler buildingHandler;

    public GameController(GamePanel gamePanel, JPanel cards, GameState gameState){
        this.cards = cards;
        CookUnitHandler cookUnitHandler = new CookUnitHandler();
        this.unitHandler = new UnitHandler(gameState, gamePanel);
        this.buildingHandler = new BuildingHandler(gameState, gamePanel, cookUnitHandler);
        TurnHandler turnHandler = new TurnHandler(gameState, gamePanel, cookUnitHandler);
        HexHandler hexHandler = new HexHandler(gameState, gamePanel, unitHandler, buildingHandler);

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
        buildingHandler.updateTaskListPanel();
        unitHandler.updateUnitListPanel();
        unitHandler.discover();
    }

}