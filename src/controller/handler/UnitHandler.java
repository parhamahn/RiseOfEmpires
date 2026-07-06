package controller.handler;

import model.GameState;
import model.Hex;
import model.building.Building;
import model.unit.Unit;
import model.unit.Worker;
import view.GamePanel;
import view.UnitStat;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnitHandler {
    private final GameState gameState;
    private final GamePanel gamePanel;

    public UnitHandler(GameState gameState, GamePanel gamePanel) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
    }

    public void handleUnitMoving(Hex clicked) {
        Unit unit = gameState.getSelectedUnit();
        if (gameState.getSelectedHex() == clicked) {
            if (unit.isWorker()) {
                handleWorkerAssignment(clicked, unit);
            } else if (unit.isBorderExpander()) {
                handleBorderExpansion(clicked, unit);
            }
        }

        if (gameState.getMap().getNeighbourHexes(gameState.getSelectedHex()).contains(clicked)) {
            if (unit.getActionPoints() >= clicked.getAPToMove()) {
                if (unit.getHex().getBuilding() != null) {
                    unit.getHex().getBuilding().getAssignedWorkers().remove(unit);
                    gameState.getProduction().recalculate(gameState);
                    gamePanel.getHudPanel().refresh();
                }
                unit.setActionPoints(unit.getActionPoints() - clicked.getAPToMove());
                unit.setTargetHex(clicked);
                unit.setMoving(true);
                unit.setAssigned(false);
            }
        }

        gameState.setSelectedUnit(null);
        gameState.setSelectedHex(clicked);
        gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        updateUnitListPanel();
    }

    private void handleWorkerAssignment(Hex clicked, Unit unit) {
        if (gameState.getSelectedHex().getBuilding() != null && !gameState.getSelectedHex().getBuilding().isTown()) {
            if (!unit.isAssigned()) {
                if (gameState.getSelectedHex().getBuilding().getAssignedWorkers().size() < gameState.getSelectedHex().getBuilding().getWorkerCapacity()) {
                    if (unit.getActionPoints() >= Building.actionPointToAssign) {
                        unit.setAssigned(true);
                        unit.setActionPoints(unit.getActionPoints() - Building.actionPointToAssign);
                        gameState.getSelectedHex().getBuilding().getAssignedWorkers().add((Worker) unit);
                        gameState.getProduction().recalculate(gameState);
                        gamePanel.getHudPanel().refresh();
                    }
                }
            } else {
                unit.setAssigned(false);
                gameState.getSelectedHex().getBuilding().getAssignedWorkers().remove(unit);
                gameState.getProduction().recalculate(gameState);
                gamePanel.getHudPanel().refresh();
            }
        }
    }

    private void handleBorderExpansion(Hex clicked, Unit unit) {
        for (Hex neighbor : gameState.getMap().getNeighbourHexes(gameState.getSelectedHex())) {
            if (!neighbor.isInEmpireBorder() && neighbor.isDiscovered()) {
                neighbor.setInEmpireBorder(true);
            }
        }
        clicked.setInEmpireBorder(true);
        gameState.getSelectedHex().getUnits().remove(unit);
        gameState.getUnits().remove(unit);
        gamePanel.getUnitListPanel().clear();
        gamePanel.getTaskListPanel().clear();
    }

    public void updateUnitListPanel() {
        Hex selected = gameState.getSelectedHex();
        if (selected != null) {
            List<UnitStat> stats = gamePanel.getUnitListPanel().updateUnits(selected.getUnits());
            for (UnitStat stat : stats) {
                stat.getButton().addActionListener(e -> {
                    gameState.setSelectedUnit(stat.getUnit());
                    gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                });
            }
            gamePanel.getTaskListPanel().clear();
        } else {
            gamePanel.getUnitListPanel().clear();
        }
    }

    public void discover() {
        Hex selected = gameState.getSelectedHex();
        if (selected != null) {
            for (Unit unit : selected.getUnits()) {
                if (unit.isExplorer()) {
                    for (Hex neighbor : gameState.getMap().getNeighbourHexes(selected)) {
                        neighbor.setDiscovered(true);
                    }
                }
            }
        }
    }
}
