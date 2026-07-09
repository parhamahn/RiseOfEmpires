package view.buildingpanel.townhall;

import helpers.ImageLoader;
import model.state.GameState;
import model.unit.Unit;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class CookSection extends JPanel {

    private final GameState gameState;
    private final Map<Unit.UNIT_TYPE, JButton> cookButtons;

    public CookSection(GameState gameState) {
        this.gameState = gameState;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JLabel label = new JLabel("Cook Units");
        label.setFont(UITheme.GAME_FONT.deriveFont(20f));
        label.setForeground(UITheme.GOLD);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(label);
        add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonsRow = new JPanel();
        buttonsRow.setOpaque(false);
        buttonsRow.setLayout(new BoxLayout(buttonsRow, BoxLayout.X_AXIS));
        buttonsRow.setAlignmentX(CENTER_ALIGNMENT);

        cookButtons = new EnumMap<>(Unit.UNIT_TYPE.class);
        for (Unit.UNIT_TYPE type : Unit.UNIT_TYPE.values()) {
            JButton button = createIconButton(unitIconPath(type));
            StyledButtons.goldBorder(button);
            cookButtons.put(type, button);
            buttonsRow.add(button);
            buttonsRow.add(Box.createRigidArea(new Dimension(8, 0)));
        }

        add(buttonsRow);
        add(Box.createVerticalGlue());
    }

    public JButton getCookButton(Unit.UNIT_TYPE type) {
        return cookButtons.get(type);
    }
    public Map<Unit.UNIT_TYPE, JButton> getCookButtons() {
        return cookButtons;
    }

    public void refresh() {
        boolean atCap = (gameState.getEmpireState().getUnits().size()
                + (int) gameState.getEmpireState().getCookQueueCount())
                >= gameState.getEmpireState().getMaxUnit();
        for (Map.Entry<Unit.UNIT_TYPE, JButton> entry : cookButtons.entrySet()) {
            JButton button = entry.getValue();
            button.setEnabled(!atCap);
            if (atCap) {
                ImageIcon normalIcon = (ImageIcon) button.getIcon();
                if (normalIcon != null) {
                    button.setDisabledIcon(new ImageIcon(
                            GrayFilter.createDisabledImage(normalIcon.getImage())));
                }
            }
        }
    }

    private String unitIconPath(Unit.UNIT_TYPE type) {
        return "/utils/" + type.toString().toLowerCase().replace("_", "") + ".png";
    }

    private JButton createIconButton(String iconPath) {
        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 64);
        JButton button = StyledButtons.iconButton(icon);
        if (gameState.getEmpireState().getMaxUnit() == gameState.getEmpireState().getUnits().size()) {
            button.setEnabled(false);
            button.setDisabledIcon(new ImageIcon(
                    GrayFilter.createDisabledImage(icon.getImage())));
        }
        return button;
    }
}
