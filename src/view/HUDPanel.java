package view;
import helpers.ImageLoader;
import model.GameState;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {

    private JButton menuButton;
    public ResourceStat woodStat;
    public ResourceStat stoneStat;
    public ResourceStat ironStat;
    public ResourceStat foodStat;

    private GameState gameState;

    public HUDPanel(GameState gameState){
        this.gameState = gameState;
        this.menuButton = new JButton("\uD804\uDC54");
        this.setBackground(new Color(0, 0, 0, 175));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        woodStat = new ResourceStat("/utils/wood.png", gameState.getStorage().getWood_quantity(), gameState.getStorage().getWood_max(), gameState.getProduction().getWood_production());
        add(woodStat);

        add(Box.createHorizontalStrut(20));

        stoneStat = new ResourceStat("/utils/stone.png", gameState.getStorage().getStone_quantity(), gameState.getStorage().getStone_max(), gameState.getProduction().getStone_production());
        add(stoneStat);

        add(Box.createHorizontalStrut(20));

        ironStat = new ResourceStat("/utils/iron.png", gameState.getStorage().getIron_quantity(), gameState.getStorage().getIron_max(), gameState.getProduction().getIron_production());
        add(ironStat);

        add(Box.createHorizontalStrut(20));

        foodStat = new ResourceStat("/utils/food.png", gameState.getStorage().getFood_quantity(), gameState.getStorage().getFood_max(), gameState.getProduction().getFood_production());
        add(foodStat);

        add(Box.createHorizontalGlue());

        this.menuButton.setPreferredSize(new Dimension(50, 50));
        this.menuButton.setMaximumSize(new Dimension(50, 50));
        this.menuButton.setMinimumSize(new Dimension(50, 50));
        this.menuButton.setFont(new Font("Serif", Font.PLAIN, 32));
        this.menuButton.setMargin(new Insets(0,0,0,0));
        this.menuButton.setBackground(new Color(255, 203, 87));
        this.menuButton.setForeground(new Color(0,0,0));
        add(menuButton);
    }

    public JButton getMenuButton() {
        return menuButton;
    }

    public ResourceStat getFoodStat() {
        return foodStat;
    }

    public void setFoodStat(ResourceStat foodStat) {
        this.foodStat = foodStat;
    }

    public ResourceStat getIronStat() {
        return ironStat;
    }

    public void setIronStat(ResourceStat ironStat) {
        this.ironStat = ironStat;
    }

    public ResourceStat getStoneStat() {
        return stoneStat;
    }

    public void setStoneStat(ResourceStat stoneStat) {
        this.stoneStat = stoneStat;
    }

    public ResourceStat getWoodStat() {
        return woodStat;
    }

    public void setWoodStat(ResourceStat woodStat) {
        this.woodStat = woodStat;
    }
}
