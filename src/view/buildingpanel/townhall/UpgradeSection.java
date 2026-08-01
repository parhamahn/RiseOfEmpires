package view.buildingpanel.townhall;

import helpers.image.ImageLoader;
import model.state.GameState;
import view.common.BaseSection;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class UpgradeSection extends BaseSection {

    private final GameState gameState;
    private final JButton upgradeButton;

    public UpgradeSection(GameState gameState) {
        super("Upgrade TownHall");
        this.gameState = gameState;
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

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
