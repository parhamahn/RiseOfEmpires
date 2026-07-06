package view;
import model.GameState;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {

    private JButton endTurnButton;
    private JButton menuButton;
    public ResourceStat woodStat;
    public ResourceStat stoneStat;
    public ResourceStat ironStat;
    public ResourceStat foodStat;
    public JLabel turnLabel;
    public JLabel unitsLabel;

    private GameState gameState;

    public HUDPanel(GameState gameState){
        this.gameState = gameState;
        this.menuButton = new JButton("\uD804\uDC54");
        this.endTurnButton = new JButton("End Turn");
        this.turnLabel = new JLabel();
        this.unitsLabel = new JLabel();
        this.setBackground(new Color(0, 0, 0, 175));
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        woodStat = new ResourceStat("/utils/wood.png", gameState.getStorage().getWoodQuantity(), gameState.getStorage().getWoodMax(), gameState.getProduction().getWoodProduction());
        add(woodStat);

        add(Box.createHorizontalStrut(20));

        stoneStat = new ResourceStat("/utils/stone.png", gameState.getStorage().getStoneQuantity(), gameState.getStorage().getStoneMax(), gameState.getProduction().getStoneProduction());
        add(stoneStat);

        add(Box.createHorizontalStrut(20));

        ironStat = new ResourceStat("/utils/iron.png", gameState.getStorage().getIronQuantity(), gameState.getStorage().getIronMax(), gameState.getProduction().getIronProduction());
        add(ironStat);

        add(Box.createHorizontalStrut(20));

        foodStat = new ResourceStat("/utils/food.png", gameState.getStorage().getFoodQuantity(), gameState.getStorage().getFoodMax(), gameState.getProduction().getFoodProduction());
        add(foodStat);

        add(Box.createHorizontalGlue());

        unitsLabel.setText("Units : "+ gameState.getUnits().size()+"/"+gameState.getMaxUnit());
        unitsLabel.setForeground(Color.WHITE);
        unitsLabel.setFont(new Font("Pixel Game", Font.PLAIN, 24));
        add(unitsLabel);

        add(Box.createHorizontalStrut(20));

        turnLabel.setText("Turn : "+ gameState.getTunrNumber());
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setFont(new Font("Pixel Game", Font.PLAIN, 24));
        add(turnLabel);

        add(Box.createHorizontalStrut(20));

        this.endTurnButton.setPreferredSize(new Dimension(200, 50));
        this.endTurnButton.setFocusable(false);
        this.endTurnButton.setMaximumSize(new Dimension(200, 50));
        this.endTurnButton.setMinimumSize(new Dimension(200, 50));
        this.endTurnButton.setFont(new Font("Pixel Game", Font.PLAIN, 32));
        this.endTurnButton.setMargin(new Insets(10,0,0,0));
        this.endTurnButton.setBackground(new Color(255, 203, 87));
        this.endTurnButton.setForeground(new Color(0,0,0));
        add(endTurnButton);

        add(Box.createHorizontalStrut(20));

        this.menuButton.setFocusable(false);
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

    public JButton getEndTurnButton() {
        return endTurnButton;
    }

    public void refresh() {
        woodStat.setCount(gameState.getStorage().getWoodQuantity(), gameState.getStorage().getWoodMax());
        stoneStat.setCount(gameState.getStorage().getStoneQuantity(), gameState.getStorage().getStoneMax());
        ironStat.setCount(gameState.getStorage().getIronQuantity(), gameState.getStorage().getIronMax());
        foodStat.setCount(gameState.getStorage().getFoodQuantity(), gameState.getStorage().getFoodMax());

        woodStat.setChange(gameState.getProduction().getWoodProduction());
        foodStat.setChange(gameState.getProduction().getFoodProduction());
        stoneStat.setChange(gameState.getProduction().getStoneProduction());
        ironStat.setChange(gameState.getProduction().getIronProduction());

        turnLabel.setText("Turn : "+ gameState.getTunrNumber());
        unitsLabel.setText("Units : "+ gameState.getUnits().size()+"/"+gameState.getMaxUnit());

        revalidate();
        repaint();
    }

}
