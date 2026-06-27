package view;

import javax.swing.*;
import java.util.List;
import model.unit.Unit;

public class UnitListPanel extends JPanel {
    public UnitListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));
    }

    public void updateUnits(List<Unit> units) {
        removeAll();
        for (Unit unit : units) {
            UnitStat stat = new UnitStat(unit);
            add(stat.getButton());
            add(Box.createVerticalStrut(5));
        }
        revalidate();
        repaint();
    }

    public void clear() {
        removeAll();
        revalidate();
        repaint();
    }
}