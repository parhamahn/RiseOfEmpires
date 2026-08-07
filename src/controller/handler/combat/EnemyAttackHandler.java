package controller.handler.combat;

import model.combat.AttackUnit;
import model.map.Hex;
import model.state.GameState;
import model.unit.Unit;
import view.combat.CombatMainPanel;
import view.game.GamePanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EnemyAttackHandler {

    private final GameState gameState;
    private final GamePanel gamePanel;

    public EnemyAttackHandler(GameState gameState, GamePanel gamePanel) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
    }

    private static class EnemyAttack {
        final Hex enemyHex;
        final Hex playerHex;

        EnemyAttack(Hex enemyHex, Hex playerHex) {
            this.enemyHex = enemyHex;
            this.playerHex = playerHex;
        }
    }

    public void handle(Runnable onComplete) {
        List<EnemyAttack> attacks = collectAttacks();
        if (attacks.isEmpty()) {
            if (onComplete != null) onComplete.run();
            return;
        }
        playNext(attacks, 0, onComplete);
    }

    private List<EnemyAttack> collectAttacks() {
        List<EnemyAttack> attacks = new ArrayList<>();
        for (Unit enemy : gameState.getWorldState().getEnemies()) {
            if (enemy.getHex() == null) continue;
            Hex target = findAdjacentPlayerHex(enemy.getHex());
            if (target != null) attacks.add(new EnemyAttack(enemy.getHex(), target));
        }
        return attacks;
    }

    private Hex findAdjacentPlayerHex(Hex enemyHex) {
        for (Hex neighbour : gameState.getWorldState().getMap().getNeighbourHexes(enemyHex)) {
            if (neighbour != null && !neighbour.getUnits().isEmpty()
                    && neighbour.getUnits().stream().anyMatch(u -> !u.getUnitType().isEnemy())) {
                return neighbour;
            }
        }
        return null;
    }

    private void playNext(List<EnemyAttack> attacks, int index, Runnable onComplete) {
        if (index >= attacks.size()) {
            gamePanel.getHudPanel().refresh();
            gamePanel.getMapPanel().markStaticDirty();
            gamePanel.getMapPanel().repaint();
            if (onComplete != null) onComplete.run();
            return;
        }

        EnemyAttack attack = attacks.get(index);

        if (attack.enemyHex.getUnits().stream().noneMatch(u -> u.getUnitType().isEnemy())
                || attack.playerHex.getUnits().isEmpty()) {
            playNext(attacks, index + 1, onComplete);
            return;
        }

        CombatMainPanel panel = gamePanel.getCombatMainPanel();
        panel.setHexes(attack.enemyHex, attack.playerHex);
        panel.setInteractive(false);
        panel.open();
        gamePanel.setAnyPanelOpen(true);
        gamePanel.setCombatActive(true);

        AttackUnit attackUnit = new AttackUnit(gameState, attack.enemyHex, attack.playerHex, true);
        attackUnit.rollDice();
        attackUnit.setPowers();

        List<Integer> attackerDices = attackUnit.getAttackerDices();
        List<Integer> defenderDices = attackUnit.getDefenderDices();
        boolean[] attackerWins = computeWins(attackerDices, defenderDices);

        panel.playRoll(attackerDices, defenderDices, attackerWins, () -> {
            if (panel.isVisible()) {
                panel.showSummary(attackUnit.getAttackerPower(), attackUnit.getDefenderPower());
            }

            Timer timer = new Timer(1400, ev -> {
                attackUnit.attack();
                if (panel.isVisible()) {
                    panel.refresh();
                }
                panel.close();
                gamePanel.setAnyPanelOpen(false);
                gamePanel.setCombatActive(false);
                playNext(attacks, index + 1, onComplete);
            });
            timer.setRepeats(false);
            timer.start();
        });
    }

    private boolean[] computeWins(List<Integer> attackerDices, List<Integer> defenderDices) {
        int pairs = Math.min(attackerDices.size(), defenderDices.size());
        boolean[] wins = new boolean[pairs];
        for (int i = 0; i < pairs; i++) {
            wins[i] = attackerDices.get(i) > defenderDices.get(i);
        }
        return wins;
    }
}
