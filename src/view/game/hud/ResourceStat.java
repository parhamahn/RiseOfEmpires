package view.game.hud;

import helpers.ImageLoader;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class ResourceStat extends JPanel {

    private JLabel iconLabel;
    private JLabel countLabel;
    private JLabel changeLabel;

    public ResourceStat(String iconPath, int count, int maximum, int change) {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 48);

        iconLabel = new JLabel(icon);
        iconLabel.setAlignmentY(CENTER_ALIGNMENT);

        countLabel = new JLabel(count +"/"+ maximum);
        countLabel.setFont(UITheme.GAME_FONT_BOLD.deriveFont(32f));
        countLabel.setForeground(Color.WHITE);
        countLabel.setAlignmentY(CENTER_ALIGNMENT);

        changeLabel = new JLabel();
        changeLabel.setFont(UITheme.GAME_FONT_BOLD.deriveFont(24f));
        updateChangeLabel(change);

        add(iconLabel);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(changeLabel);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(countLabel);
    }


    public void setCount(int newCount, int newMaximum) {
        countLabel.setText(newCount +"/"+ newMaximum);
    }
    public void setChange(int change) {
        updateChangeLabel(change);
    }


    private void updateChangeLabel(int change) {
        if (change > 0) {
            changeLabel.setText("+" + change);
            changeLabel.setForeground(UITheme.POSITIVE_CHANGE);
        } else if (change < 0) {
            changeLabel.setText(String.valueOf(change));
            changeLabel.setForeground(UITheme.NEGATIVE_CHANGE);
        } else {
            changeLabel.setText("0");
            changeLabel.setForeground(UITheme.ZERO_CHANGE);
        }
    }
}