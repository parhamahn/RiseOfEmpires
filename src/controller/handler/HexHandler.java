package controller.handler;

import controller.SelectionContext;
import controller.handler.building.bazaar.BazaarHandler;
import controller.handler.building.tradingcenter.TradingCenterHandler;
import controller.handler.building.tribe.TribeHandler;
import controller.handler.combat.CombatHandler;
import helpers.sound.SoundEffects;
import model.building.empire.Bazaar;
import model.building.other.TradingCenter;
import model.building.tribes.Tribe;
import model.item.TeleportItem;
import model.state.GameState;
import model.map.Hex;
import view.game.GamePanel;
import view.game.map.MapPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.UUID;

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

                UUID playerId = gameState.resolveActivePlayerId();


                if(selectionContext.getTeleportItemUsed()!=null){
                    TeleportItem item = selectionContext.getTeleportItemUsed();
                    item.setToHex(clicked);
                    item.effect(gameState.getEmpireState().getStorage());
                    Point p = gamePanel.getMapPanel().getHexCenter(clicked);
                    gamePanel.getMapPanel().getUnitAnimator().setPosition(item.getUnit(), p);
                    selectionContext.setTeleportItemUsed(null);
                    selectionContext.setSelectedHex(clicked);
                    unitHandler.updateUnitListPanel();
                    buildingHandler.updateTaskListPanel();
                    gamePanel.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    gamePanel.repaint();

                    return;
                }

                if (selectionContext.getWallOriginHex() != null) {
                    if(!clicked.isDiscoveredBy(playerId) || clicked.getTerrainType()== Hex.TerrainType.TALL_MOUNTAIN || clicked.getTerrainType()== Hex.TerrainType.SEA) return;
                    buildingHandler.handleWallTask(selectionContext.getWallOriginHex(), clicked);
                    gamePanel.repaint();
                    return;
                }

                if (selectionContext.getRoadOriginHex() != null) {
                    if(!clicked.isDiscoveredBy(playerId) || clicked.getTerrainType()== Hex.TerrainType.TALL_MOUNTAIN || clicked.getTerrainType()== Hex.TerrainType.SEA) return;
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
                    if (hex != null && hex.isDiscoveredBy(playerId)) {
                        discoveredNeighbour = true;
                        break;
                    }
                }

                if (selectionContext.getSelectedUnit() != null) {
                    unitHandler.handleUnitMoving(clicked);
                } else if (discoveredNeighbour) {
                    boolean ownBuilding = clicked.getBuilding() != null
                            && gameState.resolveActivePlayerId().equals(clicked.getBuilding().getOwnerID());
                    if (selectionContext.getSelectedHex() == clicked && ownBuilding && clicked.getBuilding().isTownHall()) {
                        gamePanel.setAnyPanelOpen(true);
                        gamePanel.getTownHallPanel().setSourceHex(clicked);
                        gamePanel.getTownHallPanel().open();
                    } else if (selectionContext.getSelectedHex() == clicked && ownBuilding && clicked.getBuilding() instanceof Bazaar) {
                        gamePanel.setAnyPanelOpen(true);
                        bazaarHandler.setBazaar((Bazaar) clicked.getBuilding());
                        gamePanel.getBazaarPanel().open();
                        SoundEffects.getInstance().startBazaarBackgroundSoundEffect();
                    } else if (selectionContext.getSelectedHex() == clicked && clicked.getBuilding() != null && clicked.getBuilding() instanceof TradingCenter && clicked.isDiscoveredBy(playerId) && clicked.isInEmpireBorder()) {
                        gamePanel.setAnyPanelOpen(true);
                        tradingCenterHandler.setTradingCenter((TradingCenter) clicked.getBuilding());
                        gamePanel.getTradingCenterPanel().open();
                        SoundEffects.getInstance().startBazaarBackgroundSoundEffect();
                    } else if (selectionContext.getSelectedHex() == clicked && clicked.getBuilding() != null && clicked.getBuilding() instanceof Tribe && clicked.isDiscoveredBy(playerId)) {
                        gamePanel.setAnyPanelOpen(true);
                        tribeHandler.setTribe((Tribe) clicked.getBuilding());
                        gamePanel.getTribeMainPanel().open();
                        SoundEffects.getInstance().startTribeSoundEffect();
                    }


                    selectionContext.setSelectedHex(clicked);
                    SoundEffects.getInstance().playSelectHexSoundEffect();
                    unitHandler.updateUnitListPanel();
                    unitHandler.updateItemListPanel();
                    buildingHandler.updateTaskListPanel();
                }
                gamePanel.repaint();
            }
        });
    }
}
