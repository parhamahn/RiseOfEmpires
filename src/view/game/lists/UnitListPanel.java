package view.game.lists;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import helpers.ImageLoader;
import model.unit.Unit;

import view.common.StyledButtons;

public class UnitListPanel extends JPanel {

    private JButton attackButton;

    public UnitListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));

        Icon combatIcon = ImageLoader.getInstance().loadIcon("/utils/combat.png", 32);
        this.attackButton = StyledButtons.circularButton(combatIcon, 75);
        StyledButtons.goldBorder(this.attackButton);
    }

    public JButton getAttackButton() {
        return attackButton;
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
        add(Box.createVerticalGlue());
        add(attackButton);
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
