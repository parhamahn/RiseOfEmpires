package view.common;

import helpers.ImageLoader;

import javax.swing.*;
import java.awt.*;

public class StyledButtons {


    public static JButton iconButton(Icon icon) {
        JButton button = new JButton(icon);
        UITheme.styleIconButton(button);
        return button;
    }

    public static JButton rectangularButton(String iconPath, int width, int height) {
        ImageIcon icon = ImageLoader.getInstance().loadScaledIcon(iconPath, width, height);
        JButton button = new JButton(icon);
        UITheme.styleIconButton(button);
        return button;
    }

    public static JButton menuButton(String text, Icon icon, Dimension size) {
        JButton button = new JButton(text, icon);
        UITheme.styleMenuButton(button, size);
        return button;
    }
}
