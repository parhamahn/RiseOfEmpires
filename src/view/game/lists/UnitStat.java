package view.game.lists;

import helpers.image.ImageLoader;
import model.unit.Unit;
import view.common.StyledButtons;
import view.common.UITheme;
import view.game.GameAssets;

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
                double hpf = (double) unit.getHP()/unit.getMaxHP();
                UITheme.drawHPBar(g, barX, barY-barHeight, barWidth, barHeight, hpf);
                UITheme.drawAPBar(g, barX, barY, barWidth, barHeight, apf);
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 12));
                g.drawString(String.valueOf(unit.getHealth()), getWidth()-10, getHeight());
                if (unit.isWorker() && unit.isAssigned() && (unit.getHex().getBuilding() == null || !unit.getHex().getBuilding().isTownHall())) {
                    g.setFont(new Font("sanserif", Font.PLAIN, 18));
                    g.drawString("\u2699", barX, barY - 5);
                }
            }
        };

        ImageIcon icon = ImageLoader.getInstance().loadIcon("/utils/" + GameAssets.unitImage(unit) + ".png", 60);

        button.setIcon(icon);
        UITheme.styleIconButton(button);
        StyledButtons.goldBorder(this.button);

    }


    public JButton getButton() {
        return button;
    }
    public Unit getUnit() {
        return unit;
    }

}
