package view.common;

import javax.swing.*;
import java.awt.*;

public class UITheme {

    public static final Font GAME_FONT = new Font("Pixel Game", Font.PLAIN, 24);
    public static final Font GAME_FONT_BOLD = GAME_FONT.deriveFont(Font.BOLD, 24);
    public static final Font GAME_FONT_SMALL = GAME_FONT.deriveFont(Font.PLAIN, 14);
    public static final Font GAME_FONT_LARGE = GAME_FONT.deriveFont(Font.PLAIN, 32);

    public static final Color GOLD = new Color(255, 203, 87);
    public static final Color PANEL_BG = new Color(28, 28, 28);
    public static final Color MENU_BG = new Color(38, 38, 38);
    public static final Color MENU_BUTTON_FG = new Color(178, 178, 178);

    public static final Color OVERLAY_BG = new Color(0, 0, 0, 200);
    public static final Color HUD_BG = new Color(0, 0, 0, 175);

    public static final Color POSITIVE_CHANGE = new Color(0, 175, 0);
    public static final Color NEGATIVE_CHANGE = new Color(222, 0, 19);
    public static final Color ZERO_CHANGE = new Color(248, 178, 0);

    public static final Color AP_BAR_BG = new Color(60, 60, 60);
    public static final Color AP_BAR_FG = new Color(50, 200, 50);
    public static final Color HP_BAR_BG = new Color(60, 60, 60);
    public static final Color HP_BAR_FG = new Color(255, 0, 0);

    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);


    public static void styleIconButton(JButton button) {
        button.setFocusPainted(false);
        button.setBackground(TRANSPARENT);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
    }

    public static void drawAPBar(Graphics g, int x, int y, int width, int height, double ratio) {
        g.setColor(AP_BAR_BG);
        g.fillRect(x, y, width, height);
        g.setColor(AP_BAR_FG);
        g.fillRect(x, y, (int) (ratio * width), height);
    }

    public static void drawHPBar(Graphics g, int x, int y, int width, int height, double ratio) {
        g.setColor(HP_BAR_BG);
        g.fillRect(x, y, width, height);
        g.setColor(HP_BAR_FG);
        g.fillRect(x, y, (int) (ratio * width), height);
    }
}
