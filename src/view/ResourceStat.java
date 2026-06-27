package view;

import helpers.ImageLoader;

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
        countLabel.setFont(new Font("Pixel Game", Font.BOLD, 24));
        countLabel.setForeground(Color.WHITE);
        countLabel.setAlignmentY(CENTER_ALIGNMENT);

        changeLabel = new JLabel();
        changeLabel.setFont(new Font("Pixel Game", Font.BOLD, 18));
        if (change > 0){
            changeLabel.setText("+"+change);
            changeLabel.setForeground(new Color(0, 175, 0));
        } else if (change < 0) {
            changeLabel.setText(String.valueOf(change));
            changeLabel.setForeground(new Color(222, 0, 19));
        } else if (change == 0) {
            changeLabel.setText(String.valueOf(change));
            changeLabel.setForeground(new Color(248, 178, 0));
        }

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
        if (change > 0) {
            changeLabel.setText("+" + change);
            changeLabel.setForeground(new Color(0, 175, 0));
        } else if (change < 0) {
            changeLabel.setText(String.valueOf(change));
            changeLabel.setForeground(new Color(222, 0, 19));
        } else {
            changeLabel.setText("0");
            changeLabel.setForeground(new Color(248, 178, 0));
        }
    }
}