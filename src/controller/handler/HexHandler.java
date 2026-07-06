package controller.handler;

import helpers.SoundEffects;
import model.GameState;
import model.Hex;
import view.GamePanel;
import view.MapPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HexHandler {
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final UnitHandler unitHandler;
    private final BuildingHandler buildingHandler;

    public HexHandler(GameState gameState, GamePanel gamePanel, UnitHandler unitHandler, BuildingHandler buildingHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.unitHandler = unitHandler;
        this.buildingHandler = buildingHandler;
    }

    public void attachHexSelectionListener() {
        MapPanel mapPanel = gamePanel.getMapPanel();
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Hex clicked = mapPanel.getHexAt(e.getX(), e.getY());
                if (clicked == null) return;

                boolean discoveredNeighbour = false;
                for (Hex hex : gameState.getMap().getNeighbourHexes(clicked)) {
                    if (hex != null && hex.isDiscovered()) {
                        discoveredNeighbour = true;
                        break;
                    }
                }

                if (gameState.getSelectedUnit() != null) {
                    unitHandler.handleUnitMoving(clicked);
                } else if (discoveredNeighbour) {
                    if (gameState.getSelectedHex() == clicked && clicked.getBuilding() != null && clicked.getBuilding().isTownHall()) {
                        gamePanel.getTownHallPanel().open();
                    }
                    gameState.setSelectedHex(clicked);
                    SoundEffects.getInstance().playSelectHexSoundEffect();
                    unitHandler.updateUnitListPanel();
                    buildingHandler.updateTaskListPanel();
                }
            }
        });
    }
}
