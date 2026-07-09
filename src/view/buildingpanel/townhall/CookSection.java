package view.buildingpanel.townhall;

import model.state.GameState;
import model.unit.Unit;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class CookSection extends JPanel {

    private final GameState gameState;
    private final Map<Unit.UNIT_TYPE, CookStat> cookStats;

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

        JPanel grid = new JPanel(new GridLayout(2, 4, 0, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(CENTER_ALIGNMENT);

        cookStats = new EnumMap<>(Unit.UNIT_TYPE.class);
        for (Unit.UNIT_TYPE type : Unit.UNIT_TYPE.values()) {
            boolean canAfford = type.getCookCost()==null || type.getCookCost().enoughResources(gameState.getEmpireState().getStorage());
            CookStat stat = new CookStat(type, unitIconPath(type), canAfford);
            cookStats.put(type, stat);
            grid.add(stat);
        }

        add(grid);
        add(Box.createVerticalGlue());
    }

    public JButton getCookButton(Unit.UNIT_TYPE type) {
        CookStat stat = cookStats.get(type);
        return stat != null ? stat.getCookButton() : null;
    }
    public Map<Unit.UNIT_TYPE, JButton> getCookButtons() {
        Map<Unit.UNIT_TYPE, JButton> buttons = new EnumMap<>(Unit.UNIT_TYPE.class);
        for (Map.Entry<Unit.UNIT_TYPE, CookStat> entry : cookStats.entrySet()) {
            buttons.put(entry.getKey(), entry.getValue().getCookButton());
        }
        return buttons;
    }

    public void refresh() {
        boolean atCap = (gameState.getEmpireState().getUnits().size()
                + (int) gameState.getEmpireState().getCookQueueCount())
                >= gameState.getEmpireState().getMaxUnit();
        for (Map.Entry<Unit.UNIT_TYPE, CookStat> entry : cookStats.entrySet()) {
            CookStat stat = entry.getValue();
            Unit.UNIT_TYPE type = entry.getKey();
            boolean canAfford = (type.getCookCost() == null
                    || type.getCookCost().enoughResources(gameState.getEmpireState().getStorage()))
                    && !atCap;
            stat.refresh(canAfford);
        }
        revalidate();
        repaint();
    }

    private String unitIconPath(Unit.UNIT_TYPE type) {
        return "/utils/" + type.toString().toLowerCase().replace("_", "") + ".png";
    }
}
