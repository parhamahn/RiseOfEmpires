package view.game.hud;
import model.state.GameState;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {

    private JButton endTurnButton;
    private JButton menuButton;
    private ResourceStat woodStat;
    private ResourceStat stoneStat;
    private ResourceStat ironStat;
    private ResourceStat foodStat;
    private JLabel turnLabel;
    private JLabel unitsLabel;
    private GameState gameState;


    public HUDPanel(GameState gameState){
        this.gameState = gameState;
        this.menuButton = new JButton("\uD804\uDC54");
        this.endTurnButton = new JButton("End Turn");
        this.turnLabel = new JLabel();
        this.unitsLabel = new JLabel();
        this.setBackground(UITheme.HUD_BG);
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        woodStat = new ResourceStat("/utils/wood.png", gameState.getEmpireState().getStorage().getWoodQuantity(), gameState.getEmpireState().getStorage().getWoodMax(), gameState.getEmpireState().getProduction().getWoodProduction());
        add(woodStat);

        add(Box.createHorizontalStrut(20));

        stoneStat = new ResourceStat("/utils/stone.png", gameState.getEmpireState().getStorage().getStoneQuantity(), gameState.getEmpireState().getStorage().getStoneMax(), gameState.getEmpireState().getProduction().getStoneProduction());
        add(stoneStat);

        add(Box.createHorizontalStrut(20));

        ironStat = new ResourceStat("/utils/iron.png", gameState.getEmpireState().getStorage().getIronQuantity(), gameState.getEmpireState().getStorage().getIronMax(), gameState.getEmpireState().getProduction().getIronProduction());
        add(ironStat);

        add(Box.createHorizontalStrut(20));

        foodStat = new ResourceStat("/utils/food.png", gameState.getEmpireState().getStorage().getFoodQuantity(), gameState.getEmpireState().getStorage().getFoodMax(), gameState.getEmpireState().getProduction().getFoodProduction());
        add(foodStat);

        add(Box.createHorizontalGlue());

        unitsLabel.setText("Units : "+ gameState.getEmpireState().getUnits().size()+"/"+gameState.getEmpireState().getMaxUnit());
        unitsLabel.setForeground(Color.WHITE);
        unitsLabel.setFont(UITheme.GAME_FONT);
        add(unitsLabel);

        add(Box.createHorizontalStrut(20));

        turnLabel.setText("Turn : "+ gameState.getWorldState().getTurnNumber());
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setFont(UITheme.GAME_FONT);
        add(turnLabel);

        add(Box.createHorizontalStrut(20));

        this.endTurnButton.setPreferredSize(new Dimension(200, 50));
        this.endTurnButton.setFocusable(false);
        this.endTurnButton.setMaximumSize(new Dimension(200, 50));
        this.endTurnButton.setMinimumSize(new Dimension(200, 50));
        this.endTurnButton.setFont(UITheme.GAME_FONT_LARGE);
        this.endTurnButton.setMargin(new Insets(10,0,0,0));
        this.endTurnButton.setBackground(UITheme.GOLD);
        this.endTurnButton.setForeground(Color.BLACK);
        add(endTurnButton);

        add(Box.createHorizontalStrut(20));

        this.menuButton.setFocusable(false);
        this.menuButton.setPreferredSize(new Dimension(50, 50));
        this.menuButton.setMaximumSize(new Dimension(50, 50));
        this.menuButton.setMaximumSize(new Dimension(50, 50));
        this.menuButton.setFont(new Font("Serif", Font.PLAIN, 32));
        this.menuButton.setMargin(new Insets(0,0,0,0));
        this.menuButton.setBackground(UITheme.GOLD);
        this.menuButton.setForeground(Color.BLACK);
        add(menuButton);
    }


    public JButton getMenuButton() {
        return menuButton;
    }
    public JButton getEndTurnButton() {
        return endTurnButton;
    }


    public void refresh() {
        woodStat.setCount(gameState.getEmpireState().getStorage().getWoodQuantity(), gameState.getEmpireState().getStorage().getWoodMax());
        stoneStat.setCount(gameState.getEmpireState().getStorage().getStoneQuantity(), gameState.getEmpireState().getStorage().getStoneMax());
        ironStat.setCount(gameState.getEmpireState().getStorage().getIronQuantity(), gameState.getEmpireState().getStorage().getIronMax());
        foodStat.setCount(gameState.getEmpireState().getStorage().getFoodQuantity(), gameState.getEmpireState().getStorage().getFoodMax());

        woodStat.setChange(gameState.getEmpireState().getProduction().getWoodProduction());
        foodStat.setChange(gameState.getEmpireState().getProduction().getFoodProduction());
        stoneStat.setChange(gameState.getEmpireState().getProduction().getStoneProduction());
        ironStat.setChange(gameState.getEmpireState().getProduction().getIronProduction());

        turnLabel.setText("Turn : "+ gameState.getWorldState().getTurnNumber());
        unitsLabel.setText("Units : "+ gameState.getEmpireState().getUnits().size()+"/"+gameState.getEmpireState().getMaxUnit());

        revalidate();
        repaint();
    }

}
