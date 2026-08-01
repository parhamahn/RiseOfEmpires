package view.combat;

import helpers.image.ImageLoader;
import model.map.Hex;
import model.unit.Unit;
import view.common.BaseSection;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CombatSideSection extends BaseSection {

    private List<DiceSlot> diceSlots = new ArrayList<>();
    private JPanel diceZone;
    private JPanel unitZone;
    private String title;

    public CombatSideSection(String title) {
        super(title);
        this.title = title;

        diceZone = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        diceZone.setOpaque(false);
        diceZone.setVisible(false);
        add(diceZone);

        add(Box.createRigidArea(new Dimension(0, 5)));

        unitZone = new JPanel();
        unitZone.setLayout(new BoxLayout(unitZone, BoxLayout.Y_AXIS));
        unitZone.setOpaque(false);
        add(unitZone);
    }

    public void refresh(Hex hex) {
        unitZone.removeAll();

        if (hex == null) {
            revalidate();
            repaint();
            return;
        }

        Map<Unit.UNIT_TYPE, Integer> grouped = new LinkedHashMap<>();
        for (Unit u : hex.getUnits()) {
            if (u.isCombatUnit()) {
                grouped.merge(u.getUnitType(), 1, Integer::sum);
            }
        }

        for (Map.Entry<Unit.UNIT_TYPE, Integer> entry : grouped.entrySet()) {
            JPanel row = new JPanel();
            row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
            row.setOpaque(false);

            String path = "/utils/" + entry.getKey().toString().toLowerCase() + ".png";
            ImageIcon icon = ImageLoader.getInstance().loadIcon(path, 50);
            JButton iconButton = new JButton(icon);
            UITheme.styleIconButton(iconButton);
            row.add(iconButton);

            if (entry.getValue() > 1) {
                row.add(Box.createRigidArea(new Dimension(5, 0)));
                JLabel countLabel = new JLabel("x" + entry.getValue());
                countLabel.setFont(UITheme.GAME_FONT_SMALL);
                countLabel.setForeground(UITheme.GOLD);
                row.add(countLabel);
            }

            row.add(Box.createHorizontalGlue());
            unitZone.add(row);
            unitZone.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        revalidate();
        repaint();
    }

    public void setDice(List<Integer> dice) {
        diceZone.removeAll();
        diceSlots.clear();

        for (int value : dice) {
            DiceSlot slot = new DiceSlot();
            slot.setValue(value);
            diceSlots.add(slot);
            diceZone.add(slot);
        }

        diceZone.setVisible(!dice.isEmpty());
        revalidate();
        repaint();
    }

    public void clearDice() {
        diceZone.removeAll();
        diceSlots.clear();
        diceZone.setVisible(false);
        revalidate();
        repaint();
    }

    private static class DiceSlot extends JPanel {
        private int value = 0;

        DiceSlot() {
            setPreferredSize(new Dimension(55, 55));
            setMinimumSize(new Dimension(55, 55));
            setMaximumSize(new Dimension(55, 55));
            setOpaque(false);
        }

        void setValue(int value) {
            this.value = value;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (value == 0) {
                g2.setColor(new Color(40, 40, 40));
                g2.fillRoundRect(2, 2, w - 4, h - 4, 8, 8);
                g2.setColor(new Color(80, 80, 80));
                g2.drawRoundRect(2, 2, w - 4, h - 4, 8, 8);
                g2.dispose();
                return;
            }

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(2, 2, w - 4, h - 4, 8, 8);
            g2.setColor(Color.BLACK);
            g2.drawRoundRect(2, 2, w - 4, h - 4, 8, 8);

            int pad = 14;
            int cx = w / 2;
            int cy = h / 2;
            int x1 = pad;
            int y1 = pad;
            int x2 = w - pad;
            int y2 = h - pad;

            int[][] pips;
            switch (value) {
                case 1: pips = new int[][]{{cx, cy}}; break;
                case 2: pips = new int[][]{{x2, y1}, {x1, y2}}; break;
                case 3: pips = new int[][]{{x2, y1}, {cx, cy}, {x1, y2}}; break;
                case 4: pips = new int[][]{{x1, y1}, {x2, y1}, {x1, y2}, {x2, y2}}; break;
                case 5: pips = new int[][]{{x1, y1}, {x2, y1}, {cx, cy}, {x1, y2}, {x2, y2}}; break;
                case 6: pips = new int[][]{{x1, y1}, {x2, y1}, {x1, cy}, {x2, cy}, {x1, y2}, {x2, y2}}; break;
                default: pips = new int[][]{};
            }

            int pipR = 5;
            g2.setColor(Color.BLACK);
            for (int[] p : pips) {
                g2.fillOval(p[0] - pipR, p[1] - pipR, pipR * 2, pipR * 2);
            }

            g2.dispose();
        }
    }
}
