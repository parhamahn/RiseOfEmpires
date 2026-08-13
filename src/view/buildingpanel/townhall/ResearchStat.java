package view.buildingpanel.townhall;

import helpers.image.ImageLoader;
import model.queue.Technology;
import model.storage.Cost;
import view.common.FormatUtils;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class ResearchStat extends JPanel {

    private Technology technology;
    private JButton researchButton;
    private ImageIcon buttonBg;

    public ResearchStat(Technology technology, String iconPath, String blockReason) {
        this.technology = technology;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));

        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 60);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentY(CENTER_ALIGNMENT);
        add(iconLabel);
        add(Box.createRigidArea(new Dimension(10, 0)));

        JPanel descPanel = new JPanel();
        descPanel.setOpaque(false);
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setAlignmentY(CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(FormatUtils.formatEnum(technology.toString()));
        nameLabel.setFont(UITheme.GAME_FONT.deriveFont(16f));
        nameLabel.setForeground(Color.WHITE);
        descPanel.add(nameLabel);

        Cost cost = technology.getCost();
        JLabel costLabel = new JLabel(FormatUtils.formatCost(cost, false));
        costLabel.setFont(UITheme.GAME_FONT_SMALL);
        costLabel.setForeground(Color.LIGHT_GRAY);
        descPanel.add(costLabel);

        JLabel turnLabel = new JLabel(technology.getTurnCost() + " Turns");
        turnLabel.setFont(UITheme.GAME_FONT_SMALL);
        turnLabel.setForeground(Color.LIGHT_GRAY);
        descPanel.add(turnLabel);

        add(descPanel);
        add(Box.createHorizontalGlue());

        buttonBg = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 120, 40);
        researchButton = StyledButtons.textButton("research", buttonBg, new Dimension(120, 40), 14f, UITheme.MENU_BUTTON_FG);
        researchButton.setAlignmentY(CENTER_ALIGNMENT);

        applyState(blockReason);

        add(researchButton);
    }

    public void refresh(String blockReason) {
        applyState(blockReason);
    }

    private void applyState(String blockReason) {
        boolean disabled = blockReason != null;
        StyledButtons.setDisabled(researchButton, disabled, blockReason);
        if (disabled) {
            researchButton.setDisabledIcon(UITheme.createDisabledIcon(buttonBg));
            researchButton.setBorder(null);
        } else {
            StyledButtons.goldBorder(researchButton);
        }
    }

    public JButton getResearchButton() {
        return researchButton;
    }

    public Technology getTechnology() {
        return technology;
    }
}
