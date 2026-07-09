package view.game.hud;
import helpers.ImageLoader;
import model.state.GameState;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class HUDPanel extends JPanel {

    private JButton endTurnButton;
    private ResourceStat woodStat;
    private ResourceStat stoneStat;
    private ResourceStat ironStat;
    private ResourceStat foodStat;
    private JLabel turnLabel;
    private JLabel unitsLabel;
    private GameState gameState;


    public HUDPanel(GameState gameState){
        this.gameState = gameState;
        ImageIcon endTurnIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 200, 50);
        this.endTurnButton = StyledButtons.textButton("End Turn", endTurnIcon, new Dimension(200, 50), 32f, UITheme.GOLD);
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


        add(endTurnButton);

        add(Box.createHorizontalStrut(20));

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
