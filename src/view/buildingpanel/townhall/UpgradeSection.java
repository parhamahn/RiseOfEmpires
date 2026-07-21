package view.buildingpanel.townhall;

import helpers.ImageLoader;
import model.state.GameState;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class UpgradeSection extends JPanel {

    private final GameState gameState;
    private final JButton upgradeButton;

    public UpgradeSection(GameState gameState) {
        this.gameState = gameState;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

        JLabel label = new JLabel("Upgrade TownHall");
        label.setFont(UITheme.GAME_FONT.deriveFont(32f));
        label.setForeground(UITheme.GOLD);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 15)));

        ImageIcon upgradeIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 200, 56);
        upgradeButton = StyledButtons.textButton(
                "Upgrade to " + gameState.getEmpireState().getTownHallLevel().nextUpgrade(),
                upgradeIcon, new Dimension(200, 56), 16f, UITheme.MENU_BUTTON_FG);
        upgradeButton.setAlignmentX(CENTER_ALIGNMENT);
        add(upgradeButton);
        add(Box.createVerticalGlue());
    }

    public JButton getUpgradeButton() {
        return upgradeButton;
    }

    public void refresh() {
        if (gameState.getEmpireState().getTownHallLevel().nextUpgrade() != null
                && !gameState.getEmpireState().hasUpgradeQueued()
                && gameState.getEmpireState().getQueues().isEmpty()) {
            upgradeButton.setText("Upgrade to " + gameState.getEmpireState().getTownHallLevel().nextUpgrade());
            upgradeButton.setEnabled(true);
        } else {
            upgradeButton.setEnabled(false);
        }
    }
}
