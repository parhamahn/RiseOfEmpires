package controller.handler;

import controller.SelectionContext;
import helpers.SoundEffects;
import model.state.GameState;
import model.map.Hex;
import view.game.GamePanel;
import view.game.map.MapPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HexHandler {
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final UnitHandler unitHandler;
    private final BuildingHandler buildingHandler;
    private final SelectionContext selectionContext;

    public HexHandler(GameState gameState, GamePanel gamePanel, UnitHandler unitHandler, BuildingHandler buildingHandler, SelectionContext selectionContext) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.unitHandler = unitHandler;
        this.buildingHandler = buildingHandler;
        this.selectionContext = selectionContext;
    }

    public void attachHexSelectionListener() {
        MapPanel mapPanel = gamePanel.getMapPanel();
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Hex clicked = mapPanel.getHexAt(e.getX(), e.getY());
                if (clicked == null) return;

                boolean discoveredNeighbour = false;
                for (Hex hex : gameState.getWorldState().getMap().getNeighbourHexes(clicked)) {
                    if (hex != null && hex.isDiscovered()) {
                        discoveredNeighbour = true;
                        break;
                    }
                }

                if (selectionContext.getSelectedUnit() != null) {
                    unitHandler.handleUnitMoving(clicked);
                } else if (discoveredNeighbour) {
                    if (selectionContext.getSelectedHex() == clicked && clicked.getBuilding() != null && clicked.getBuilding().isTownHall()) {
                        gamePanel.getTownHallPanel().open();
                    }
                    selectionContext.setSelectedHex(clicked);
                    SoundEffects.getInstance().playSelectHexSoundEffect();
                    unitHandler.updateUnitListPanel();
                    buildingHandler.updateTaskListPanel();
                }
            }
        });
    }
}
