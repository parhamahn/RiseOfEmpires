package controller.handler;

import controller.SelectionContext;
import controller.handler.building.bazaar.BazaarHandler;
import controller.handler.building.tradingcenter.TradingCenterHandler;
import controller.handler.building.tribe.TribeHandler;
import controller.handler.combat.CombatHandler;
import helpers.SoundEffects;
import model.building.empire.Bazaar;
import model.building.other.TradingCenter;
import model.building.tribes.Tribe;
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
    private final BazaarHandler bazaarHandler;
    private final TradingCenterHandler tradingCenterHandler;
    private final TribeHandler tribeHandler;
    private final CombatHandler combatHandler;

    public HexHandler(GameState gameState, GamePanel gamePanel, UnitHandler unitHandler, BuildingHandler buildingHandler, SelectionContext selectionContext, BazaarHandler bazaarHandler, TradingCenterHandler tradingCenterHandler, TribeHandler tribeHandler, CombatHandler combatHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.unitHandler = unitHandler;
        this.buildingHandler = buildingHandler;
        this.selectionContext = selectionContext;
        this.bazaarHandler = bazaarHandler;
        this.tradingCenterHandler = tradingCenterHandler;
        this.tribeHandler = tribeHandler;
        this.combatHandler = combatHandler;
    }

    public void attachHexSelectionListener() {
        MapPanel mapPanel = gamePanel.getMapPanel();
        mapPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Hex clicked = mapPanel.getHexAt(e.getX(), e.getY());
                if (clicked == null) return;
                if(gamePanel.isAnyPanelOpen())return;

                if (selectionContext.getWallOriginHex() != null) {
                    buildingHandler.handleWallTask(selectionContext.getWallOriginHex(), clicked);
                    gamePanel.repaint();
                    return;
                }

                if (selectionContext.getRoadOriginHex() != null) {
                    buildingHandler.handleRoadTask(selectionContext.getRoadOriginHex(), clicked);
                    gamePanel.repaint();
                    return;
                }

                if (selectionContext.getDestroyWallOriginHex() != null) {
                    buildingHandler.handleWallDestroy(selectionContext.getDestroyWallOriginHex(), clicked);
                    gamePanel.repaint();
                    return;
                }

                if (selectionContext.getDestroyRoadOriginHex() != null) {
                    buildingHandler.handleRoadDestroy(selectionContext.getDestroyRoadOriginHex(), clicked);
                    gamePanel.repaint();
                    return;
                }

                if (selectionContext.getAttackerHex() != null) {
                    combatHandler.handleAttackTarget(selectionContext.getAttackerHex(), clicked);
                    gamePanel.repaint();
                    return;
                }

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
                        gamePanel.setAnyPanelOpen(true);
                        gamePanel.getTownHallPanel().open();
                    } else if (selectionContext.getSelectedHex() == clicked && clicked.getBuilding() != null && clicked.getBuilding() instanceof Bazaar) {
                        gamePanel.setAnyPanelOpen(true);
                        bazaarHandler.setBazaar((Bazaar) clicked.getBuilding());
                        gamePanel.getBazaarPanel().open();
                    } else if (selectionContext.getSelectedHex() == clicked && clicked.getBuilding() != null && clicked.getBuilding() instanceof TradingCenter && clicked.isInEmpireBorder()) {
                        gamePanel.setAnyPanelOpen(true);
                        tradingCenterHandler.setTradingCenter((TradingCenter) clicked.getBuilding());
                        gamePanel.getTradingCenterPanel().open();
                    } else if (selectionContext.getSelectedHex() == clicked && clicked.getBuilding() != null && clicked.getBuilding() instanceof Tribe) {
                        gamePanel.setAnyPanelOpen(true);
                        tribeHandler.setTribe((Tribe) clicked.getBuilding());
                        gamePanel.getTribeMainPanel().open();
                    }


                    selectionContext.setSelectedHex(clicked);
                    SoundEffects.getInstance().playSelectHexSoundEffect();
                    unitHandler.updateUnitListPanel();
                    buildingHandler.updateTaskListPanel();
                }
                gamePanel.repaint();
            }
        });
    }
}
