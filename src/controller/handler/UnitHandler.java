package controller.handler;

import controller.SelectionContext;
import model.Season;
import model.state.GameState;
import model.map.Hex;
import model.building.Building;
import model.unit.Unit;
import model.unit.Worker;
import view.game.GamePanel;
import view.game.lists.UnitStat;

import java.awt.*;
import java.util.List;

public class UnitHandler {
    private final GameState gameState;
    private final GamePanel gamePanel;
    private final SelectionContext selectionContext;
    private final BuildingHandler buildingHandler;

    public UnitHandler(GameState gameState, GamePanel gamePanel, SelectionContext selectionContext, BuildingHandler buildingHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.selectionContext = selectionContext;
        this.buildingHandler = buildingHandler;
    }

    public void handleUnitMoving(Hex clicked) {
        Unit unit = selectionContext.getSelectedUnit();
        if (selectionContext.getSelectedHex() == clicked) {
            if (unit.isWorker()) {
                handleWorkerAssignment(clicked, unit);
            } else if (unit.isBorderExpander()) {
                handleBorderExpansion(clicked, unit);
            }
        }

        int frozenRoads = 0;
        if (gameState.getEmpireState().getSeason()== Season.SeasonType.WINTER){frozenRoads+=1;}

        if (gameState.getWorldState().getMap().getNeighbourHexes(selectionContext.getSelectedHex()).contains(clicked)) {
            if (unit.getActionPoints() >= (clicked.getAPToMove()+frozenRoads)) {
                if (unit.getHex().getBuilding() != null) {
                    unit.getHex().getBuilding().getAssignedWorkers().remove(unit);
                    gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
                    gamePanel.getHudPanel().refresh();
                }
                unit.setActionPoints(unit.getActionPoints() - (clicked.getAPToMove()+frozenRoads));
                unit.setTargetHex(clicked);
                if (unit.getPositionX() == 0 && unit.getPositionY() == 0 && unit.getHex() != null) {
                    Point p = gamePanel.getMapPanel().getHexCenter(unit.getHex());
                    unit.setPositionX(p.x);
                    unit.setPositionY(p.y);
                }
                unit.setMoving(true);
                unit.setAssigned(false);
            }
        }

        selectionContext.setSelectedUnit(null);
        selectionContext.setSelectedHex(clicked);
        gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        updateUnitListPanel();
        buildingHandler.updateTaskListPanel();
    }

    private void handleWorkerAssignment(Hex clicked, Unit unit) {
        if (selectionContext.getSelectedHex().getBuilding() != null && !selectionContext.getSelectedHex().getBuilding().isTown()) {
            if (!unit.isAssigned()) {
                if (selectionContext.getSelectedHex().getBuilding().getAssignedWorkers().size() < selectionContext.getSelectedHex().getBuilding().getWorkerCapacity()) {
                    if (unit.getActionPoints() >= Building.actionPointToAssign) {
                        unit.setAssigned(true);
                        unit.setActionPoints(unit.getActionPoints() - Building.actionPointToAssign);
                        selectionContext.getSelectedHex().getBuilding().getAssignedWorkers().add((Worker) unit);
                        gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
                        gamePanel.getHudPanel().refresh();
                    }
                }
            } else {
                unit.setAssigned(false);
                selectionContext.getSelectedHex().getBuilding().getAssignedWorkers().remove(unit);
                gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
                gamePanel.getHudPanel().refresh();
            }
        }
    }

    private void handleBorderExpansion(Hex clicked, Unit unit) {
        for (Hex neighbor : gameState.getWorldState().getMap().getNeighbourHexes(selectionContext.getSelectedHex())) {
            if (!neighbor.isInEmpireBorder() && neighbor.isDiscovered()) {
                neighbor.setInEmpireBorder(true);
            }
        }
        clicked.setInEmpireBorder(true);
        selectionContext.getSelectedHex().getUnits().remove(unit);
        gameState.getEmpireState().getUnits().remove(unit);
        gamePanel.getUnitListPanel().clear();
        gamePanel.getTaskListPanel().clear();
        gamePanel.getMapPanel().markStaticDirty();
    }

    public void updateUnitListPanel() {
        Hex selected = selectionContext.getSelectedHex();
        if (selected != null) {
            List<UnitStat> stats = gamePanel.getUnitListPanel().updateUnits(selected.getUnits());
            for (UnitStat stat : stats) {
                stat.getButton().addActionListener(e -> {
                    selectionContext.setSelectedUnit(stat.getUnit());
                    gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    gamePanel.repaint();
                });
            }
            gamePanel.getTaskListPanel().clear();
        } else {
            gamePanel.getUnitListPanel().clear();
        }
    }

    public void discover() {
        Hex selected = selectionContext.getSelectedHex();
        if (selected != null) {
            boolean discoveredAny = false;
            for (Unit unit : selected.getUnits()) {
                if (unit.isExplorer()) {
                    for (Hex neighbor : gameState.getWorldState().getMap().getNeighbourHexes(selected)) {
                        if (!neighbor.isDiscovered()) {
                            neighbor.setDiscovered(true);
                            discoveredAny = true;
                        }
                    }
                }
            }
            if (discoveredAny) {
                gamePanel.getMapPanel().markStaticDirty();
            }
        }
    }
}
