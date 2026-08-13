package view.buildingpanel.tribe;

import helpers.image.ImageLoader;
import model.building.tribes.Tribe;
import model.state.GameState;
import view.common.BaseSection;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TribeActionSection extends BaseSection {

    private JButton sendGiftButton;
    private JButton startTrade;
    private JButton getMission;
    private JButton doMission;
    private JButton declareWar;
    private JButton askPeace;
    private JButton askAlliance;
    private JButton viewReward;
    private final GameState gameState;

    public TribeActionSection(GameState gameState) {
        super("Actions", 40);
        this.gameState = gameState;
        List<JButton> buttons = new ArrayList<>();


        int spaceSize = 30;
        float fontSize = 21f;
        Icon icon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png",200, 43);
        Dimension dimension = new Dimension(icon.getIconWidth(), icon.getIconHeight());

        sendGiftButton = StyledButtons.textButton("Send Gift", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        startTrade = StyledButtons.textButton("Trade", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        getMission = StyledButtons.textButton("Get Mission", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        doMission = StyledButtons.textButton("Do Mission", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        declareWar = StyledButtons.textButton("Declare War", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        askPeace = StyledButtons.textButton("Ask Peace",  icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        askAlliance = StyledButtons.textButton("Ask Alliance",  icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        viewReward = StyledButtons.textButton("View Reward", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);

        buttons.add(sendGiftButton);
        buttons.add(startTrade);
        buttons.add(getMission);
        buttons.add(doMission);
        buttons.add(declareWar);
        buttons.add(askPeace);
        buttons.add(askAlliance);
        buttons.add(viewReward);



        for (JButton button : buttons) {
            add(button);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(Box.createRigidArea(new Dimension(0, spaceSize)));
        }

    }

    public void reset(Tribe tribe){

        if(!tribe.isCanDeclareWar()){
            StyledButtons.setDisabled(declareWar, true, "Cannot declare war with this tribe");
            declareWar.setBorder(null);
        }else{
            StyledButtons.goldBorder(declareWar);
            StyledButtons.setDisabled(declareWar, false, null);
        }
        if(!tribe.isCanGetMission()){
            StyledButtons.setDisabled(getMission, true, "Cannot take a mission from this tribe");
            getMission.setBorder(null);
        }else if(tribe.getActiveMission()!=null){
            StyledButtons.setDisabled(getMission, true, "You already have an active mission");
            getMission.setBorder(null);
        }else{
            StyledButtons.goldBorder(getMission);
            StyledButtons.setDisabled(getMission, false, null);
        }
        if(tribe.getActiveMission()==null){
            StyledButtons.setDisabled(doMission, true, "No active mission");
        }else if(!tribe.getActiveMission().isAffordable(gameState)){
            StyledButtons.setDisabled(doMission, true, "Not enough resources for the mission");
        }else if(!tribe.getActiveMission().canReward(gameState)){
            StyledButtons.setDisabled(doMission, true, "Not enough storage capacity for the reward");
        }else {
            StyledButtons.goldBorder(doMission);
            StyledButtons.setDisabled(doMission, false, null);
        }
        if(!tribe.isCanTrade()){
            StyledButtons.setDisabled(startTrade, true, "Cannot trade with this tribe");
            startTrade.setBorder(null);
        }else if(tribe.getTradeStrategy() == null){
            StyledButtons.setDisabled(startTrade, true, "This tribe has no trade offer");
            startTrade.setBorder(null);
        }else{
            StyledButtons.goldBorder(startTrade);
            StyledButtons.setDisabled(startTrade, false, null);
        }
        if(!tribe.isCanGift()){
            StyledButtons.setDisabled(sendGiftButton, true, "Cannot send gifts to this tribe");
            sendGiftButton.setBorder(null);
        }else{
            StyledButtons.goldBorder(sendGiftButton);
            StyledButtons.setDisabled(sendGiftButton, false, null);
        }
        if(!tribe.isCanRequestPeace()){
            StyledButtons.setDisabled(askPeace, true, "Cannot request peace with this tribe");
            askPeace.setBorder(null);
        }else{
            StyledButtons.goldBorder(askPeace);
            StyledButtons.setDisabled(askPeace, false, null);
        }
        if(!tribe.isCanRequestAlliance(gameState.getEmpireState().getAllies())){
            StyledButtons.setDisabled(askAlliance, true, "Cannot request an alliance with this tribe");
            askAlliance.setBorder(null);
        }else{
            StyledButtons.goldBorder(askAlliance);
            StyledButtons.setDisabled(askAlliance, false, null);
        }
        if(!tribe.isDiscovered()){
            StyledButtons.setDisabled(viewReward, true, "Tribe is not discovered yet");
            viewReward.setBorder(null);
        }else{
            StyledButtons.goldBorder(viewReward);
            StyledButtons.setDisabled(viewReward, false, null);
        }

    }

    public JButton getSendGiftButton() {
        return sendGiftButton;
    }

    public JButton getStartTrade() {
        return startTrade;
    }

    public JButton getGetMission() {
        return getMission;
    }

    public JButton getDoMission() {
        return doMission;
    }

    public JButton getDeclareWar() {
        return declareWar;
    }

    public JButton getAskPeace() {
        return askPeace;
    }

    public JButton getAskAlliance() {
        return askAlliance;
    }

    public JButton getViewReward() {
        return viewReward;
    }
}
