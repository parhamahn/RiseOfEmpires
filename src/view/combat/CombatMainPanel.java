package view.combat;

import helpers.image.ImageLoader;
import model.map.Hex;
import model.state.GameState;
import view.common.BaseModalPanel;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CombatMainPanel extends BaseModalPanel {

    private JButton attackButton;
    private CombatSideSection attackerSideSection;
    private CombatSideSection defenderSideSection;
    private JLabel summaryLabel;
    private Hex attackerHex;
    private Hex defenderHex;
    private boolean interactive = true;

    public CombatMainPanel(GameState gameState) {
        super(gameState, "Combat", "/utils/combatpanel.png");
    }

    @Override
    protected JPanel buildBody() {
        int BORDER_SPACE = 20;
        JPanel content  = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(BORDER_SPACE, BORDER_SPACE, BORDER_SPACE, BORDER_SPACE));

        attackerSideSection = new CombatSideSection("Attacker");
        defenderSideSection = new CombatSideSection("Defender");

        JPanel sides = new JPanel();
        sides.setLayout(new BoxLayout(sides, BoxLayout.X_AXIS));
        sides.setOpaque(false);
        sides.add(attackerSideSection);
        sides.add(Box.createHorizontalGlue());
        sides.add(defenderSideSection);

        summaryLabel = new JLabel(" ");
        summaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        summaryLabel.setFont(UITheme.GAME_FONT_BOLD.deriveFont(20f));
        summaryLabel.setForeground(UITheme.GOLD);
        summaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        float fontSize = 24f;
        Icon icon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png",240, 55);
        Dimension dimension = new Dimension(icon.getIconWidth(), icon.getIconHeight());
        attackButton = StyledButtons.textButton("Attack", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);

        attackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sides.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(sides);
        content.add(Box.createVerticalGlue());
        content.add(summaryLabel);
        content.add(Box.createRigidArea(new Dimension(0, 10)));
        content.add(attackButton);

        return  content;
    }

    @Override
    public void refresh() {
        attackButton.setEnabled(interactive);
        attackerSideSection.refresh(attackerHex);
        defenderSideSection.refresh(defenderHex);
    }

    public void setHexes(Hex attackerHex, Hex defenderHex) {
        this.attackerHex = attackerHex;
        this.defenderHex = defenderHex;
        clearSummary();
        attackerSideSection.clearDice();
        defenderSideSection.clearDice();
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
        attackButton.setEnabled(interactive && attackerHex != null);
    }

    public void playRoll(List<Integer> attackerDice, List<Integer> defenderDice,
                         boolean[] attackerWins, Runnable onDone) {
        boolean[] defenderWins = new boolean[attackerWins.length];
        for (int i = 0; i < attackerWins.length; i++) defenderWins[i] = !attackerWins[i];

        int[] pending = {2};
        Runnable done = () -> {
            pending[0]--;
            if (pending[0] == 0 && onDone != null) onDone.run();
        };

        attackerSideSection.animateTo(attackerDice, attackerWins, done);
        defenderSideSection.animateTo(defenderDice, defenderWins, done);
    }

    public void showSummary(int attackerPower, int defenderPower) {
        int attackerRounds = attackerPower;
        int defenderRounds = defenderPower;
        String winner;
        if (attackerPower > defenderPower) {
            winner = "Attacker wins!";
        } else if (defenderPower > attackerPower) {
            winner = "Defender wins!";
        } else {
            winner = "Draw!";
        }
        summaryLabel.setText(String.format(
                "Attacker won %d round(s) \u2192 %d damage   |   Defender won %d round(s) \u2192 %d damage   \u2014   %s",
                attackerRounds, attackerPower, defenderRounds, defenderPower, winner));
    }

    public void clearSummary() {
        summaryLabel.setText(" ");
    }

    public Hex getAttackerHex() {
        return attackerHex;
    }

    public Hex getDefenderHex() {
        return defenderHex;
    }

    public JButton getAttackButton() {
        return attackButton;
    }

    public CombatSideSection getAttackerSideSection() {
        return attackerSideSection;
    }

    public CombatSideSection getDefenderSideSection() {
        return defenderSideSection;
    }
}
