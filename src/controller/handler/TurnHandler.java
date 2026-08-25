package controller.handler;

import controller.handler.building.townhall.CookUnitHandler;
import controller.handler.combat.EnemyAttackHandler;
import helpers.sound.SoundEffects;
import model.Season;
import model.disasters.Disaster;
import model.engine.GameEvent;
import model.engine.TurnEngine;
import model.state.EmpireState;
import model.state.GameState;
import permanency.SlotID;
import permanency.save.SaveManager;
import view.common.WarningDialogs;
import view.game.GamePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TurnHandler {

    private final GameState gameState;
    private final GamePanel gamePanel;
    private final DisasterHandler disasterHandler;
    private final EnemyAttackHandler enemyAttackHandler;

    public TurnHandler(GameState gameState, GamePanel gamePanel, CookUnitHandler cookUnitHandler, BuildingHandler buildingHandler, DisasterHandler disasterHandler) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.disasterHandler = disasterHandler;
        this.enemyAttackHandler = new EnemyAttackHandler(gameState, gamePanel);
    }

    public void endTurnAction() {
        EmpireState actor = gameState.getEmpireState();
        if (TurnEngine.hasUnspentActionPoints(actor)) {
            int x = WarningDialogs.unitsWithActionPoints();
            if (x == JOptionPane.NO_OPTION) {
                return;
            }
        }

        actor.applyTurnEndHappiness();

        List<GameEvent> events = new ArrayList<>(TurnEngine.endTurnFor(gameState, actor));
        List<Disaster> disasters = List.of();
        if (gameState.isLastInRound()) {
            disasters = TurnEngine.rollAndApplyDisasters(gameState);
            events.addAll(TurnEngine.worldTick(gameState));
        }
        events.addAll(TurnEngine.processQueues(gameState, actor));

        gameState.nextPlayerTurn();

        renderEvents(events);
        disasterHandler.animate(disasters);

        enemyAttackHandler.handle(() -> {
            gamePanel.getTownHallPanel().refresh();
            gamePanel.getHudPanel().refresh();
            gamePanel.getUnitListPanel().clear();
            gamePanel.getTaskListPanel().clear();
            gamePanel.getMapPanel().markStaticDirty();
            gamePanel.repaint();
            if (actor.isUpkeepDisaster()) {
                WarningDialogs.upkeepDisaster();
            }
            if (actor.isStarving()) {
                WarningDialogs.starvationDisaster();
            }

            try {
                SaveManager.writeSave(gameState, SlotID.AUTO_SAVE, "Auto_Save");
            } catch (Exception e) {
                WarningDialogs.autosaveFailed(e.getMessage());
            }
        });
    }

    private void renderEvents(List<GameEvent> events) {
        boolean mapNeedsRefresh = false;
        for (GameEvent event : events) {
            switch (event.getType()) {
                case MISSION_FAILED -> WarningDialogs.missionFailed(event.getDetail());
                case NEW_MISSION_AVAILABLE -> WarningDialogs.newTradeOrMission(event.getDetail());
                case GUARD_SPAWNED -> WarningDialogs.tribeProducedUnit(event.getDetail());
                case TRIBE_NOW_ENEMY ->
                        WarningDialogs.tribeWarning(event.getDetail() + " has become your enemy because you built near its camp!");
                case SEASON_CHANGED -> {
                    String[] parts = event.getDetail().split("->");
                    SoundEffects.getInstance().seasonSoundEffect(
                            Season.SeasonType.valueOf(parts[0]), Season.SeasonType.valueOf(parts[1]));
                }
                case QUEUE_COMPLETED -> mapNeedsRefresh = true;
                default -> { }
            }
        }
        if (mapNeedsRefresh) {
            gamePanel.getMapPanel().markStaticDirty();
            gamePanel.getMapPanel().repaint();
        }
    }
}
