package view.game.lists;

import helpers.ImageLoader;
import model.unit.Unit;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class UnitStat {

    private Unit unit;
    private JButton button;


    public UnitStat(Unit unit) {
        this.unit = unit;
        this.button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int barWidth = getWidth() / 4 * 3;
                int barHeight = 4;
                int barX = 6;
                int barY = getHeight() - barHeight;
                double apf = (double) unit.getActionPoints() / unit.getActionPointsMax();
                UITheme.drawBar(g, barX, barY, barWidth, barHeight, apf);
                if (unit.isWorker() && unit.isAssigned() && (unit.getHex().getBuilding() == null || !unit.getHex().getBuilding().isTownHall())) {
                    g.setFont(new Font("sanserif", Font.PLAIN, 18));
                    g.drawString("\u2699", barX, barY - 5);
                }
            }
        };

        ImageIcon icon = ImageLoader.getInstance().loadIcon("/utils/" + unit.getUnitImageName() + ".png", 75);

        button.setIcon(icon);
        UITheme.styleIconButton(button);

    }


    public JButton getButton() {
        return button;
    }
    public Unit getUnit() {
        return unit;
    }

}
