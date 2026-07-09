package view.buildingpanel.townhall;

import helpers.ImageLoader;
import model.queue.Technology;
import model.storage.Cost;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class ResearchStat extends JPanel {

    private Technology technology;
    private JButton researchButton;
    private ImageIcon buttonBg;

    public ResearchStat(Technology technology, String iconPath, boolean canAfford, boolean alreadyResearched) {
        this.technology = technology;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 60);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentY(CENTER_ALIGNMENT);
        add(iconLabel);
        add(Box.createRigidArea(new Dimension(10, 0)));

        JPanel descPanel = new JPanel();
        descPanel.setOpaque(false);
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setAlignmentY(CENTER_ALIGNMENT);

        JLabel nameLabel = new JLabel(formatName(technology));
        nameLabel.setFont(UITheme.GAME_FONT.deriveFont(16f));
        nameLabel.setForeground(Color.WHITE);
        descPanel.add(nameLabel);

        Cost cost = technology.getCost();
        JLabel costLabel = new JLabel(formatCost(cost));
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

        applyState(canAfford, alreadyResearched);

        add(researchButton);
    }

    public void refresh(boolean canAfford, boolean alreadyResearched) {
        applyState(canAfford, alreadyResearched);
    }

    private void applyState(boolean canAfford, boolean alreadyResearched) {
        boolean disabled = !canAfford || alreadyResearched;
        researchButton.setEnabled(!disabled);
        if (disabled) {
            researchButton.setDisabledIcon(new ImageIcon(
                    GrayFilter.createDisabledImage(buttonBg.getImage())));
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


    private String formatName(Technology technology) {
        String name = technology.toString();
        StringBuilder formatted = new StringBuilder();
        boolean nextUpper = true;
        for (char c : name.toCharArray()) {
            if (c == '_') {
                formatted.append(' ');
                nextUpper = true;
            } else if (nextUpper) {
                formatted.append(Character.toUpperCase(c));
                nextUpper = false;
            } else {
                formatted.append(Character.toLowerCase(c));
            }
        }
        return formatted.toString();
    }

    private String formatCost(Cost cost) {
        StringBuilder sb = new StringBuilder("Cost: ");
        boolean first = true;
        if (cost.getWoodNeeded() > 0) {
            sb.append(cost.getWoodNeeded()).append(" Wood");
            first = false;
        }
        if (cost.getStoneNeeded() > 0) {
            if (!first) sb.append(", ");
            sb.append(cost.getStoneNeeded()).append(" Stone");
            first = false;
        }
        if (cost.getIronNeeded() > 0) {
            if (!first) sb.append(", ");
            sb.append(cost.getIronNeeded()).append(" Iron");
        }
        if (first) sb.append("None");
        return sb.toString();
    }
}
