package view;

import javax.swing.*;
import java.awt.*;

public class ResourceStat extends JPanel {
    private JLabel iconLabel;
    private JLabel countLabel;
    private JLabel maxLabel;

    public ResourceStat(ImageIcon icon, int count, int maximum) {

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        iconLabel = new JLabel(icon);
        iconLabel.setAlignmentY(CENTER_ALIGNMENT);

        countLabel = new JLabel(count +"/"+ maximum);
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        countLabel.setForeground(Color.WHITE);
        countLabel.setAlignmentY(CENTER_ALIGNMENT);

        add(iconLabel);
        add(Box.createRigidArea(new Dimension(5, 0)));
        add(countLabel);
    }

    public void setCount(int newCount, int newMaximum) {
        countLabel = new JLabel(newCount +"/"+ newMaximum);
    }

    public void setIcon(ImageIcon newIcon) {
        iconLabel.setIcon(newIcon);
    }
}