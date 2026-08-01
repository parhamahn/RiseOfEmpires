package view.buildingpanel.tribe;

import helpers.image.ImageLoader;
import model.building.tribes.Tribe;
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

    public TribeActionSection() {
        super("Actions", 40);
        List<JButton> buttons = new ArrayList<>();


        int spaceSize = 30;
        float fontSize = 24f;
        Icon icon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png",240, 55);
        Dimension dimension = new Dimension(icon.getIconWidth(), icon.getIconHeight());

        sendGiftButton = StyledButtons.textButton("Send Gift", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        startTrade = StyledButtons.textButton("Trade", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        getMission = StyledButtons.textButton("Get Mission", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        doMission = StyledButtons.textButton("Do Mission", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        declareWar = StyledButtons.textButton("Declare War", icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        askPeace = StyledButtons.textButton("Ask Peace",  icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);
        askAlliance = StyledButtons.textButton("Ask Alliance",  icon, dimension, fontSize, UITheme.MENU_BUTTON_FG);

        buttons.add(sendGiftButton);
        buttons.add(startTrade);
        buttons.add(getMission);
        buttons.add(doMission);
        buttons.add(declareWar);
        buttons.add(askPeace);
        buttons.add(askAlliance);



        for (JButton button : buttons) {
            add(button);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(Box.createRigidArea(new Dimension(0, spaceSize)));
        }

    }

    public void reset(Tribe tribe){

        if(!tribe.isCanDeclareWar()){declareWar.setEnabled(false); declareWar.setBorder(null);}else{
            StyledButtons.goldBorder(declareWar);
            declareWar.setEnabled(true);
        }
        if(!tribe.isCanGetMission()){getMission.setEnabled(false);  getMission.setBorder(null);}else{
            StyledButtons.goldBorder(getMission);
            getMission.setEnabled(true);
        }
        if(tribe.getActiveMission()==null){ doMission.setEnabled(false); }else {
            StyledButtons.goldBorder(doMission);
            doMission.setEnabled(true);
        }
        if(!tribe.isCanTrade() || tribe.getTradeStrategy() == null){startTrade.setEnabled(false);   startTrade.setBorder(null);}else{
            StyledButtons.goldBorder(startTrade);
            startTrade.setEnabled(true);
        }
        if(!tribe.isCanGift()){sendGiftButton.setEnabled(false);  sendGiftButton.setBorder(null);}else{
            StyledButtons.goldBorder(sendGiftButton);
            sendGiftButton.setEnabled(true);
        }
        if(!tribe.isCanRequestPeace()){askPeace.setEnabled(false);   askPeace.setBorder(null);}else{
            StyledButtons.goldBorder(askPeace);
            askPeace.setEnabled(true);
        }
        if(!tribe.isCanRequestAlliance()){askAlliance.setEnabled(false);   askAlliance.setBorder(null);}else{
            StyledButtons.goldBorder(askAlliance);
            askAlliance.setEnabled(true);
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
}
