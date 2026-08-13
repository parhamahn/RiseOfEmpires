package controller.handler;

import controller.SelectionContext;
import helpers.sound.SoundEffects;
import model.queue.Technology;
import model.state.GameState;
import model.map.Hex;
import model.building.Building;
import model.building.ProducingBuilding;
import model.building.tribes.Tribe;
import model.unit.Unit;
import model.unit.civilian.Worker;
import view.game.GamePanel;
import view.game.lists.UnitStat;
import view.game.map.UnitAnimator;

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
        if (unit == null || unit.getUnitType().isEnemy()) {
            selectionContext.setSelectedUnit(null);
            return;
        }
        if (selectionContext.getSelectedHex() == clicked) {
            if (unit.isWorker()) {
                handleWorkerAssignment(clicked, unit);
            } else if (unit.isBorderExpander()) {
                handleBorderExpansion(clicked, unit);
            }
        }

        if(clicked.getTerrainType()== Hex.TerrainType.SEA && !gameState.getEmpireState().getResearchedTechnologies().contains(Technology.SAILING)){return;}

        if (gameState.getWorldState().getMap().getNeighbourHexes(selectionContext.getSelectedHex()).contains(clicked)) {
            int APNeeded = unit.getHex().getMoveAPCostTo(clicked, gameState.getEmpireState().getSeason());

            if (unit.getActionPoints() >= APNeeded) {
                if (unit.getHex().getBuilding() instanceof ProducingBuilding producing) {
                    producing.getAssignedWorkers().remove(unit);
                    gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
                    gamePanel.getHudPanel().refresh();
                }
                unit.setActionPoints(unit.getActionPoints() - APNeeded);
                if(clicked.getTerrainType()== Hex.TerrainType.SEA){unit.setActionPoints(0); gamePanel.getMapPanel().markStaticDirty();}
                unit.setTargetHex(clicked);
                UnitAnimator animator = gamePanel.getMapPanel().getUnitAnimator();        if(clicked.getTerrainType()== Hex.TerrainType.SEA){unit.setActionPoints(0);}

                if (animator.getPosition(unit) == null && unit.getHex() != null) {
                    Point p = gamePanel.getMapPanel().getHexCenter(unit.getHex());
                    animator.setPosition(unit, p);
                }
                unit.setMoving(true);
                unit.setAssigned(false);
                SoundEffects.getInstance().playUnitMoveSoundEffect();
            }
        }

        selectionContext.setSelectedUnit(null);
        selectionContext.setSelectedHex(clicked);
        gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        updateUnitListPanel();
        buildingHandler.updateTaskListPanel();
    }

    private void handleWorkerAssignment(Hex clicked, Unit unit) {
        if (selectionContext.getSelectedHex().getBuilding() instanceof Building building && !building.isTown()) {
            if (building instanceof ProducingBuilding producing) {
                if (!unit.isAssigned()) {
                    if (producing.getAssignedWorkers().size() < producing.getWorkerCapacity()) {
                        if (unit.getActionPoints() >= Building.actionPointToAssign) {
                            unit.setAssigned(true);
                            unit.setActionPoints(unit.getActionPoints() - Building.actionPointToAssign);
                            producing.getAssignedWorkers().add((Worker) unit);
                            gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
                            gamePanel.getHudPanel().refresh();
                        }
                    }
                } else {
                    unit.setAssigned(false);
                    producing.getAssignedWorkers().remove(unit);
                    gameState.getEmpireState().recalculateProduction(gameState.getWorldState().getMap());
                    gamePanel.getHudPanel().refresh();
                }
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
        gameState.getEmpireState().removeUnit(unit);
        gamePanel.getUnitListPanel().clear();
        gamePanel.getTaskListPanel().clear();
        gamePanel.getMapPanel().markStaticDirty();
    }

    public void updateUnitListPanel() {
        Hex selected = selectionContext.getSelectedHex();
        if (selected != null) {
            List<UnitStat> stats = gamePanel.getUnitListPanel().updateUnits(selected.getUnits());
            for (UnitStat stat : stats) {
                if (stat.getUnit().getUnitType().isEnemy()) {
                    continue;
                }
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
        if (selected == null) return;

        boolean discoveredAny = false;

        for (Unit unit : selected.getUnits()) {
            if (unit.isExplorer()) {
                for (Hex neighbor : gameState.getWorldState().getMap().getNeighbourHexes(selected)) {
                    if (neighbor != null && !neighbor.isDiscovered()) {
                        neighbor.setDiscovered(true);
                        markStructureDiscovered(neighbor);
                        discoveredAny = true;
                    }
                }
            }
        }

        if (!selected.isDiscovered()) {
            selected.setDiscovered(true);
            markStructureDiscovered(selected);
            discoveredAny = true;
        }

        if (discoveredAny) {
            gamePanel.getMapPanel().markStaticDirty();
        }
    }

    private void markStructureDiscovered(Hex hex) {
        if (hex.getBuilding() instanceof Tribe tribe) {
            tribe.setDiscovered(true);
        }
    }
}
