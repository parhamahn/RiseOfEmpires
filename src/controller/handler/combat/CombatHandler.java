package controller.handler.combat;

import controller.SelectionContext;
import model.building.tribes.Tribe;
import model.combat.AttackBuilding;
import model.combat.AttackUnit;
import model.map.Hex;
import model.state.GameState;
import model.unit.Unit;
import view.combat.CombatMainPanel;
import view.common.StyledButtons;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.List;

public class CombatHandler {

    private GameState gameState;
    private GamePanel gamePanel;
    private SelectionContext selectionContext;
    private CombatMain combatMain;
    private AttackUnit pendingAttack;
    private ActionListener attackListener;


    public CombatHandler(GameState gameState, GamePanel gamePanel,  SelectionContext selectionContext) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.selectionContext = selectionContext;
        this.combatMain = new CombatMain(gameState, gamePanel, selectionContext);
    }


    public void attachHandlers(){
        attachAttackButton();
        attachCombatActionButton();
        combatMain.attachHandlers();
    }

    public void handleAttackTarget(Hex attackerHex, Hex defenderHex){

        AttackUnit probe = new AttackUnit(gameState, attackerHex, defenderHex);
        int distance = probe.getDistance();

        if (distance == 0 || distance == -1 || !probe.isLegal()) {
            selectionContext.setAttackerHex(null);
            return;
        }

        gameState.recordTribeAttack(attackerHex, defenderHex);

        boolean defenderHasCombatUnits = defenderHex.getUnits().stream().anyMatch(Unit::isCombatUnit);
        boolean defenderHasBuilding = defenderHex.getBuilding() != null;
        boolean defenderHasAnyUnits = !defenderHex.getUnits().isEmpty();

        if (defenderHasBuilding && !defenderHasCombatUnits) {
            handleBuildingAttack(attackerHex, defenderHex);
            return;
        }

        if (!defenderHasAnyUnits) {
            if (distance == 1) {
                handleAutoCapture(attackerHex, defenderHex);
            } else {
                selectionContext.setAttackerHex(null);
            }
            return;
        }

        gamePanel.getCombatMainPanel().setHexes(attackerHex, defenderHex);
        gamePanel.getCombatMainPanel().setInteractive(true);
        gamePanel.getCombatMainPanel().open();
        gamePanel.setAnyPanelOpen(true);
        gamePanel.setCombatActive(true);

    }

    private void handleAutoCapture(Hex attackerHex, Hex defenderHex) {
        for (Unit u : attackerHex.getUnits()) {
            if (u.isCombatUnit() && u.getActionPoints()>0) u.setActionPoints(u.getActionPoints() - 1);
        }

        if(defenderHex.getBuilding() != null && defenderHex.getBuilding() instanceof Tribe){
            java.util.UUID owner = gameState.resolveActivePlayerId();
            for(Hex neigh:gameState.getWorldState().getMap().getNeighbourHexes(defenderHex)){
                neigh.setBorderOwner(owner);
            }
            defenderHex.setBorderOwner(owner);
        }

        selectionContext.setAttackerHex(null);
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
    }

    private void handleBuildingAttack(Hex attackerHex, Hex defenderHex) {
        AttackBuilding attack = new AttackBuilding(gameState, attackerHex, defenderHex);
        attack.attack();

        selectionContext.setAttackerHex(null);
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
    }


    public GameState getGameState() {
        return gameState;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void attachAttackButton(){
        gamePanel.getUnitListPanel().getAttackButton().addActionListener(e->{

            selectionContext.clearAllModes();
            selectionContext.setAttackerHex(selectionContext.getSelectedHex());
            gamePanel.repaint();

        });
    }

    private void attachCombatActionButton() {
        attackListener = e -> {
            CombatMainPanel panel = gamePanel.getCombatMainPanel();
            Hex attackerHex = panel.getAttackerHex();
            Hex defenderHex = panel.getDefenderHex();
            if (attackerHex == null || defenderHex == null) return;

            if (defenderHex.getUnits().isEmpty()) {
                closeCombat(panel);
                return;
            }

            pendingAttack = new AttackUnit(gameState, attackerHex, defenderHex);
            if (!pendingAttack.isLegal()) {
                selectionContext.setAttackerHex(null);
                return;
            }
            pendingAttack.rollDice();
            pendingAttack.setPowers();

            StyledButtons.setDisabled(panel.getAttackButton(), true, "Resolving combat...");
            panel.clearSummary();

            List<Integer> attackerDices = pendingAttack.getAttackerDices();
            List<Integer> defenderDices = pendingAttack.getDefenderDices();
            boolean[] attackerWins = computeWins(attackerDices, defenderDices);

            panel.playRoll(attackerDices, defenderDices, attackerWins, () -> {
                if (!panel.isVisible()) return;
                panel.showSummary(pendingAttack.getAttackerPower(), pendingAttack.getDefenderPower());

                Timer timer = new Timer(1400, ev -> {
                    if (!panel.isVisible()) return;
                    pendingAttack.attack();
                    selectionContext.setAttackerHex(null);
                    panel.refresh();

                    if (defenderHex.getUnits().isEmpty()) {
                        closeCombat(panel);
                        return;
                    }

                    boolean hasAP = attackerHex.getUnits().stream()
                            .filter(Unit::isCombatUnit)
                            .anyMatch(u -> u.getActionPoints() > 0);
                    if (!hasAP) StyledButtons.setDisabled(panel.getAttackButton(), true, "No action points left");
                    gamePanel.getHudPanel().refresh();
                    gamePanel.getMapPanel().markStaticDirty();
                    gamePanel.getMapPanel().repaint();
                });
                timer.setRepeats(false);
                timer.start();
            });
        };
        gamePanel.getCombatMainPanel().getAttackButton().addActionListener(attackListener);
    }

    private boolean[] computeWins(List<Integer> attackerDices, List<Integer> defenderDices) {
        int pairs = Math.min(attackerDices.size(), defenderDices.size());
        boolean[] wins = new boolean[pairs];
        for (int i = 0; i < pairs; i++) {
            wins[i] = attackerDices.get(i) > defenderDices.get(i);
        }
        return wins;
    }

    private void closeCombat(CombatMainPanel panel) {
        selectionContext.setAttackerHex(null);
        panel.close();
        gamePanel.setAnyPanelOpen(false);
        gamePanel.setCombatActive(false);
        gamePanel.getHudPanel().refresh();
        gamePanel.getMapPanel().markStaticDirty();
        gamePanel.getMapPanel().repaint();
    }


}
