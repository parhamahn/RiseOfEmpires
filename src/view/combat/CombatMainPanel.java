package view.combat;

import helpers.ImageLoader;
import model.map.Hex;
import model.state.GameState;
import view.common.BaseModalPanel;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class CombatMainPanel extends BaseModalPanel {

    private JButton attackButton;
    private CombatSideSection attackerSideSection;
    private CombatSideSection defenderSideSection;
    private Hex attackerHex;
    private Hex defenderHex;

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

        float fontSize = 24f;
        Icon icon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png",240, 55);
        Dimension dimension = new Dimension(icon.getIconWidth(), icon.getIconHeight());
        attackButton = StyledButtons.textButton("Attack", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);

        attackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sides.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(sides);
        content.add(Box.createVerticalGlue());
        content.add(attackButton);

        return  content;
    }

    @Override
    public void refresh() {
        attackButton.setEnabled(true);
        attackerSideSection.refresh(attackerHex);
        defenderSideSection.refresh(defenderHex);
    }

    public void setHexes(Hex attackerHex, Hex defenderHex) {
        this.attackerHex = attackerHex;
        this.defenderHex = defenderHex;
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