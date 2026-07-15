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
    private JLabel cvunitsLabel;
    private JLabel cbunitsLabel;
    private JLabel happinessLabel;
    private JLabel seasonLabel;
    private GameState gameState;


    public HUDPanel(GameState gameState){
        this.gameState = gameState;
        ImageIcon endTurnIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 200, 50);
        this.endTurnButton = StyledButtons.textButton("End Turn", endTurnIcon, new Dimension(200, 50), 32f, UITheme.GOLD);
        this.turnLabel = new JLabel();
        this.cvunitsLabel = new JLabel();
        this.cbunitsLabel = new JLabel();
        this.happinessLabel = new JLabel();
        this.seasonLabel = new JLabel();
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

        if (gameState.getEmpireState().getHappiness() > 0) {
            this.happinessLabel.setText("Happiness : +" + gameState.getEmpireState().getHappiness());
            this.happinessLabel.setForeground(UITheme.POSITIVE_CHANGE);
        } else if (gameState.getEmpireState().getHappiness() < 0) {
            this.happinessLabel.setText("Happiness : "+String.valueOf(gameState.getEmpireState().getHappiness()));
            this.happinessLabel.setForeground(UITheme.NEGATIVE_CHANGE);
        } else {
            this.happinessLabel.setText("Happiness : 0");
            this.happinessLabel.setForeground(UITheme.ZERO_CHANGE);
        }
        this.happinessLabel.setFont(UITheme.GAME_FONT);
        add(this.happinessLabel);

        add(Box.createHorizontalGlue());

        cvunitsLabel.setText("Civil Units : "+ gameState.getEmpireState().getCivilUnitCount()+"/"+gameState.getEmpireState().getMaxUnit());
        cvunitsLabel.setForeground(Color.WHITE);
        cvunitsLabel.setFont(UITheme.GAME_FONT);
        add(cvunitsLabel);

        add(Box.createHorizontalStrut(20));

        cbunitsLabel.setText("Combat units : "+ gameState.getEmpireState().getCombatUnitCount()+"/"+gameState.getEmpireState().getTownHallLevel().getCombatUnitCap());
        cbunitsLabel.setForeground(Color.WHITE);
        cbunitsLabel.setFont(UITheme.GAME_FONT);
        add(cbunitsLabel);

        add(Box.createHorizontalStrut(20));

        turnLabel.setText("Turn : "+ gameState.getWorldState().getTurnNumber());
        turnLabel.setForeground(Color.WHITE);
        turnLabel.setFont(UITheme.GAME_FONT);
        add(turnLabel);

        add(Box.createHorizontalStrut(20));

        switch (gameState.getEmpireState().getSeason()){
            case SPRING -> seasonLabel.setIcon(ImageLoader.getInstance().loadScaledIcon("/utils/spring.png", 50, 50));
            case SUMMER -> seasonLabel.setIcon(ImageLoader.getInstance().loadScaledIcon("/utils/summer.png", 50, 50));
            case FALL -> seasonLabel.setIcon(ImageLoader.getInstance().loadScaledIcon("/utils/fall.png", 50, 50));
            case WINTER -> seasonLabel.setIcon(ImageLoader.getInstance().loadScaledIcon("/utils/winter.png", 50, 50));
        }

        add(this.seasonLabel);

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
        cvunitsLabel.setText("Units : "+ gameState.getEmpireState().getCivilUnitCount()+"/"+gameState.getEmpireState().getMaxUnit());
        cbunitsLabel.setText("Combat units : "+ gameState.getEmpireState().getCombatUnitCount()+"/"+gameState.getEmpireState().getTownHallLevel().getCombatUnitCap());


        if (gameState.getEmpireState().getHappiness() > 0) {
            this.happinessLabel.setText("Happiness : +" + gameState.getEmpireState().getHappiness());
            this.happinessLabel.setForeground(UITheme.POSITIVE_CHANGE);
        } else if (gameState.getEmpireState().getHappiness() < 0) {
            this.happinessLabel.setText("Happiness : "+String.valueOf(gameState.getEmpireState().getHappiness()));
            this.happinessLabel.setForeground(UITheme.NEGATIVE_CHANGE);
        } else {
            this.happinessLabel.setText("Happiness : 0");
            this.happinessLabel.setForeground(UITheme.ZERO_CHANGE);
        }

        switch (gameState.getEmpireState().getSeason()){
            case SPRING -> seasonLabel.setIcon(ImageLoader.getInstance().loadScaledIcon("/utils/spring.png", 50, 50));
            case SUMMER -> seasonLabel.setIcon(ImageLoader.getInstance().loadScaledIcon("/utils/summer.png", 50, 50));
            case FALL -> seasonLabel.setIcon(ImageLoader.getInstance().loadScaledIcon("/utils/fall.png", 50, 50));
            case WINTER -> seasonLabel.setIcon(ImageLoader.getInstance().loadScaledIcon("/utils/winter.png", 50, 50));
        }

        repaint();
    }

}
