package view.common;

import helpers.image.ImageLoader;
import model.Season;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class UITheme {

    public static final Font baseFont;

    static {
        try {
            baseFont = Font.createFont(Font.TRUETYPE_FONT, UITheme.class.getResourceAsStream("/utils/Pixel Game.otf"));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Font GAME_FONT = baseFont.deriveFont(Font.PLAIN, 24);
    public static final Font GAME_FONT_BOLD = baseFont.deriveFont(Font.BOLD, 24);
    public static final Font GAME_FONT_SMALL = baseFont.deriveFont(Font.PLAIN, 14);
    public static final Font GAME_FONT_LARGE = baseFont.deriveFont(Font.PLAIN, 32);

    public static final Color GOLD = new Color(255, 203, 87);
    public static final Color PANEL_BG = new Color(28, 28, 28);
    public static final Color MENU_BG = new Color(38, 38, 38);
    public static final Color MENU_BUTTON_FG = new Color(178, 178, 178);

    public static final Color OVERLAY_BG = new Color(0, 0, 0, 200);
    public static final Color PANEL_OVERLAY = new Color(0, 0, 0, 180);
    public static final Color HUD_BG = new Color(0, 0, 0, 175);

    public static final Color POSITIVE_CHANGE = new Color(0, 175, 0);
    public static final Color NEGATIVE_CHANGE = new Color(222, 0, 19);
    public static final Color ZERO_CHANGE = new Color(248, 178, 0);

    public static final Color AP_BAR_BG = new Color(60, 60, 60);
    public static final Color AP_BAR_FG = new Color(50, 200, 50);
    public static final Color HP_BAR_BG = new Color(60, 60, 60);
    public static final Color HP_BAR_FG = new Color(255, 0, 0);

    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    public static final ImageIcon CIRCLE_BUTTON = ImageLoader.getInstance().loadIcon("/utils/circlemenubutton.png", 64);

    public static ImageIcon createDisabledIcon(ImageIcon icon) {
        return new ImageIcon(GrayFilter.createDisabledImage(icon.getImage()));
    }

    public static ImageIcon getSeasonIcon(Season.SeasonType season) {
        return ImageLoader.getInstance().loadScaledIcon(
                "/utils/" + season.name().toLowerCase() + ".png", 50, 50);
    }

    public static void formatChangeLabel(JLabel label, int value, String prefix) {
        if (value > 0) {
            label.setText(prefix + "+" + value);
            label.setForeground(POSITIVE_CHANGE);
        } else if (value < 0) {
            label.setText(prefix + value);
            label.setForeground(NEGATIVE_CHANGE);
        } else {
            label.setText(prefix + "0");
            label.setForeground(ZERO_CHANGE);
        }
    }

    public static void formatChangeLabel(JLabel label, int value) {
        formatChangeLabel(label, value, "");
    }

    public static JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(GAME_FONT.deriveFont(32f));
        label.setForeground(GOLD);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

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
