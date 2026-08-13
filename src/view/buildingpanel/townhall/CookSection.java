package view.buildingpanel.townhall;

import model.state.GameState;
import model.unit.Unit;
import view.common.BaseSection;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

public class CookSection extends BaseSection {

    private final GameState gameState;
    private final Map<Unit.UNIT_TYPE, CookStat> cookStats;

    public CookSection(GameState gameState) {
        super("Cook Units");
        this.gameState = gameState;
        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel grid = new JPanel(new GridLayout(2, 4, 0, 0));
        grid.setOpaque(false);
        grid.setAlignmentX(CENTER_ALIGNMENT);

        cookStats = new EnumMap<>(Unit.UNIT_TYPE.class);
        for (Unit.UNIT_TYPE type : Unit.UNIT_TYPE.values()) {
            if(type== Unit.UNIT_TYPE.BEAR || type== Unit.UNIT_TYPE.BARBARIAN || type== Unit.UNIT_TYPE.GUARD){continue;}
            String blockReason = gameState.getEmpireState().cookBlockReason(type);
            CookStat stat = new CookStat(type, unitIconPath(type), blockReason);
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
        for (Map.Entry<Unit.UNIT_TYPE, CookStat> entry : cookStats.entrySet()) {
            CookStat stat = entry.getValue();
            Unit.UNIT_TYPE type = entry.getKey();
            stat.refresh(gameState.getEmpireState().cookBlockReason(type));
        }
        revalidate();
        repaint();
    }

    private String unitIconPath(Unit.UNIT_TYPE type) {
        return "/utils/" + type.toString().toLowerCase().replace("_", "") + ".png";
    }
}
