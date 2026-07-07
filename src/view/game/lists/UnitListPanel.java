package view.game.lists;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import model.unit.Unit;

public class UnitListPanel extends JPanel {

    public UnitListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));
    }

    public List<UnitStat> updateUnits(List<Unit> units) {
        removeAll();
        List<UnitStat> stats = new ArrayList<>();
        for (Unit unit : units) {
            UnitStat stat = new UnitStat(unit);
            stats.add(stat);
            add(stat.getButton());
            add(Box.createVerticalStrut(5));
        }
        revalidate();
        repaint();
        return stats;
    }

    public void clear() {
        removeAll();
        revalidate();
        repaint();
    }
}