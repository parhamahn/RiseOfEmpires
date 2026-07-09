package view.buildingpanel.townhall;

import helpers.ImageLoader;
import model.storage.Cost;
import model.unit.Unit;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class CookStat extends JPanel {

    private Unit.UNIT_TYPE unitType;
    private JButton cookButton;

    public CookStat(Unit.UNIT_TYPE unitType, String iconPath, boolean canAfford) {
        this.unitType = unitType;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setAlignmentY(CENTER_ALIGNMENT);

        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 64);
        cookButton = StyledButtons.iconButton(icon);
        cookButton.setAlignmentX(CENTER_ALIGNMENT);
        StyledButtons.goldBorder(cookButton);
        add(cookButton);

        JLabel nameLabel = new JLabel(formatName(unitType));
        nameLabel.setFont(UITheme.GAME_FONT.deriveFont(15f));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(nameLabel);

        Cost cost = unitType.getCookCost();
        JLabel costLabel = new JLabel(formatCostLine(cost));
        costLabel.setFont(UITheme.GAME_FONT_SMALL.deriveFont(13f));
        costLabel.setForeground(Color.LIGHT_GRAY);
        costLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(costLabel);

        JLabel turnLabel = new JLabel(unitType.getTurnCost() + " turn");
        turnLabel.setFont(UITheme.GAME_FONT_SMALL.deriveFont(13f));
        turnLabel.setForeground(Color.LIGHT_GRAY);
        turnLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(turnLabel);

        applyState(canAfford);
    }

    public void refresh(boolean canAfford) {
        applyState(canAfford);
    }

    private void applyState(boolean canAfford) {
        cookButton.setEnabled(canAfford);
        if (!canAfford) {
            ImageIcon icon = (ImageIcon) cookButton.getIcon();
            if (icon != null) {
                cookButton.setDisabledIcon(new ImageIcon(
                        GrayFilter.createDisabledImage(icon.getImage())));
            }
        }
    }

    public JButton getCookButton() {
        return cookButton;
    }

    public Unit.UNIT_TYPE getUnitType() {
        return unitType;
    }

    private String formatName(Unit.UNIT_TYPE unitType) {
        String name = unitType.toString();
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

    private String formatCostLine(Cost cost) {
        if (cost == null) return "";
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        if (cost.getWoodNeeded() > 0) {
            sb.append(cost.getWoodNeeded()).append("W");
            first = false;
        }
        if (cost.getStoneNeeded() > 0) {
            if (!first) sb.append(" ");
            sb.append(cost.getStoneNeeded()).append("S");
            first = false;
        }
        if (cost.getIronNeeded() > 0) {
            if (!first) sb.append(" ");
            sb.append(cost.getIronNeeded()).append("I");
            first = false;
        }
        if (cost.getFoodNeeded() > 0) {
            if (!first) sb.append(" ");
            sb.append(cost.getFoodNeeded()).append("F");
            first = false;
        }
        return sb.toString();
    }
}
