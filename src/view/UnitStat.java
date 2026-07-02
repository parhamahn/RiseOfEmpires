package view;

import helpers.ImageLoader;
import model.unit.Unit;

import javax.swing.*;
import java.awt.*;

public class UnitStat{

    private Unit unit;
    private JButton button;

    public UnitStat(Unit unit){
        this.unit = unit;
        this.button = new JButton(){
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                int barWidth = getWidth()/4*3;
                int barHeight = 4;
                int barX = 6;
                int barY = getHeight()-barHeight;
                double apf = (double)unit.getActionPoints()/unit.getActionPointsMax();
                g.setColor(new Color(60,60,60));
                g.fillRect(barX, barY, barWidth, barHeight);
                g.setColor(new Color(50,200,50));
                g.fillRect(barX, barY, (int)(apf*barWidth), barHeight);
            }
        };

        String iconPath = "/utils/"+unit.getClass().getName().toLowerCase().replace("model.", "").replace("unit.", "") + ".png";
        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 75);

        button.setIcon(icon);
        button.setFocusPainted(false);
        button.setBackground(new Color(0,0,0,0));
        button.setMargin(new Insets(0,0,0,0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

    }

    public JButton getButton() {
        return button;
    }

    public Unit getUnit() {
        return unit;
    }
}
