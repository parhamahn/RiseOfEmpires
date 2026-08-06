package controller.handler.combat;

import controller.SelectionContext;
import model.combat.AttackBuilding;
import model.combat.AttackUnit;
import model.map.Hex;
import model.state.GameState;
import model.unit.Unit;
import view.combat.CombatMainPanel;
import view.game.GamePanel;

import javax.swing.*;
import java.awt.event.ActionListener;

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

        boolean anyLongDistance = selectionContext.getAttackerHex().getUnits().stream().anyMatch(unit -> unit.getAttackRadius()>=2);
        int distance = gameState.getWorldState().getMap().getDistance(attackerHex, defenderHex);

        if(distance == 0 || (distance==2 && !anyLongDistance) || distance==-1){
            selectionContext.setAttackerHex(null);
            return;
        }

        boolean defenderHasCombatUnits = defenderHex.getUnits().stream().anyMatch(Unit::isCombatUnit);
        boolean defenderHasBuilding = defenderHex.getBuilding() != null;

        if (!defenderHasCombatUnits && distance == 1 && !defenderHasBuilding) {
            handleAutoCapture(attackerHex, defenderHex);
            return;
        }

        if (!defenderHasCombatUnits && defenderHasBuilding) {
            handleBuildingAttack(attackerHex, defenderHex);
            return;
        }

        gamePanel.getCombatMainPanel().setHexes(attackerHex, defenderHex);
        gamePanel.getCombatMainPanel().open();
        gamePanel.setAnyPanelOpen(true);
        gamePanel.setCombatActive(true);

    }

    private void handleAutoCapture(Hex attackerHex, Hex defenderHex) {
        for (Unit u : attackerHex.getUnits()) {
            if (u.isCombatUnit() && u.getActionPoints()>0) u.setActionPoints(u.getActionPoints() - 1);
        }

        defenderHex.setInEmpireBorder(true);

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

            if (defenderHex.getUnits().stream().noneMatch(Unit::isCombatUnit)) {
                panel.close();
                gamePanel.setAnyPanelOpen(false);
                gamePanel.setCombatActive(false);
                gamePanel.getHudPanel().refresh();
                gamePanel.getMapPanel().markStaticDirty();
                gamePanel.getMapPanel().repaint();
                return;
            }

            pendingAttack = new AttackUnit(gameState, attackerHex, defenderHex);
            pendingAttack.rollDice();
            pendingAttack.setPowers();

            panel.getAttackerSideSection().setDice(pendingAttack.getAttackerDices());
            panel.getDefenderSideSection().setDice(pendingAttack.getDefenderDices());
            panel.getAttackButton().setEnabled(false);

            Timer timer = new Timer(1500, ev -> {
                if (!panel.isVisible()) return;
                pendingAttack.attack();
                selectionContext.setAttackerHex(null);
                panel.refresh();

                if (defenderHex.getUnits().stream().noneMatch(Unit::isCombatUnit)) {
                    panel.close();
                    gamePanel.setAnyPanelOpen(false);
                    gamePanel.setCombatActive(false);
                    gamePanel.getHudPanel().refresh();
                    gamePanel.getMapPanel().markStaticDirty();
                    gamePanel.getMapPanel().repaint();
                    return;
                }

                boolean hasAP = attackerHex.getUnits().stream()
                        .filter(Unit::isCombatUnit)
                        .anyMatch(u -> u.getActionPoints() > 0);
                if (!hasAP) panel.getAttackButton().setEnabled(false);
                gamePanel.getHudPanel().refresh();
                gamePanel.getMapPanel().markStaticDirty();
                gamePanel.getMapPanel().repaint();
            });
            timer.setRepeats(false);
            timer.start();
        };
        gamePanel.getCombatMainPanel().getAttackButton().addActionListener(attackListener);
    }


}
