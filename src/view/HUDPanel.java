package view;
import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {

    private JButton menuButton;
    private ResourceStat woodStat;
    private ResourceStat stoneStat;
    private ResourceStat ironStat;
    private ResourceStat foodStat;

    public HUDPanel(){
        this.menuButton = new JButton("\uD804\uDC54");
        this.setBackground(new Color(0, 0, 0, 150));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ImageIcon woodIcon = loadIcon("/utils/wood.png");
        woodStat = new ResourceStat(woodIcon, 0, 10);
        add(woodStat);

        add(Box.createHorizontalStrut(20));

        ImageIcon stoneIcon = loadIcon("/utils/stone.png");
        stoneStat = new ResourceStat(stoneIcon, 0, 10);
        add(stoneStat);

        add(Box.createHorizontalStrut(20));

        ImageIcon ironIcon = loadIcon("/utils/iron.png");
        ironStat = new ResourceStat(ironIcon, 0, 10);
        add(ironStat);

        add(Box.createHorizontalStrut(20));

        ImageIcon foodIcon = loadIcon("/utils/food.png");
        foodStat = new ResourceStat(foodIcon, 0, 10);
        add(foodStat);

        add(Box.createHorizontalGlue());

        this.menuButton.setPreferredSize(new Dimension(50, 50));
        this.menuButton.setMaximumSize(new Dimension(50, 50));
        this.menuButton.setMinimumSize(new Dimension(50, 50));
        add(menuButton);
    }

    private ImageIcon loadIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon raw = new ImageIcon(imgURL);
            Image resized = raw.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            return new ImageIcon(resized);
        } else {
            System.err.println("Couldn't find icon: " + path);
            return new ImageIcon();
        }
    }

    public JButton getMenuButton() {
        return menuButton;
    }
}
