package view.game.lists;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import helpers.ImageLoader;
import model.unit.Unit;

import view.common.StyledButtons;
import view.common.UITheme;

public class UnitListPanel extends JPanel {

    private JButton attackButton;

    public UnitListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));

        Icon combatIcon = ImageLoader.getInstance().loadIcon("/utils/combat.png", 32);
        this.attackButton = createCircularButton(combatIcon, 75);
    }

    public JButton getAttackButton() {
        return attackButton;
    }

    private JButton createCircularButton(Icon overlayIcon, int size) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2.drawImage(UITheme.CIRCLE_BUTTON.getImage(), 0, 0, w, h, this);

                if (overlayIcon != null) {
                    int iw = overlayIcon.getIconWidth();
                    int ih = overlayIcon.getIconHeight();
                    g2.drawImage(((ImageIcon) overlayIcon).getImage(), (w - iw) / 2, (h - ih) / 2, this);
                }

                g2.dispose();
            }
        };

        Dimension d = new Dimension(size, size);
        button.setPreferredSize(d);
        button.setMinimumSize(d);
        button.setMaximumSize(d);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setOpaque(false);
        StyledButtons.goldBorder(button);

        return button;
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