package view;

import helpers.ImageLoader;
import model.Unit;

import javax.swing.*;
import java.awt.*;

public class UnitStat{

    private Unit unit;
    private JButton button;

    public UnitStat(Unit unit){
        this.unit = unit;
        this.button = new JButton();

        String iconPath = "/utils/"+unit.getClass().getName().toLowerCase().replace("model.", "") + ".png";
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
}
