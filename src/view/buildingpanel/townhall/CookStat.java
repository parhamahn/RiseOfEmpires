package view.buildingpanel.townhall;

import helpers.ImageLoader;
import model.storage.Cost;
import model.unit.Unit;
import view.common.FormatUtils;
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

        JLabel nameLabel = new JLabel(FormatUtils.formatEnum(unitType.toString()));
        nameLabel.setFont(UITheme.GAME_FONT.deriveFont(15f));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(nameLabel);

        Cost cost = unitType.getCookCost();
        JLabel costLabel = new JLabel(FormatUtils.formatCost(cost, true));
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
}
