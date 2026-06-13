package controller;

import helpers.Music;
import view.SettingsPanel;

import javax.swing.*;
import java.awt.*;

public class SettingsController {
    private SettingsPanel settingsPanel;
    private JPanel cards;

    public SettingsController(SettingsPanel settingsPanel, JPanel cards){
        this.settingsPanel = settingsPanel;
        this.cards = cards;

        this.attachListeners();
    }

    public void attachListeners(){
        settingsPanel.getBackButton().addActionListener(e -> backAction());
        settingsPanel.getSoundSlider().addChangeListener(e -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                float volume = source.getValue() / 100.0f;
                Music.getInstance().setVolume(volume);
            }
        });
    }

    public void backAction(){
        CardLayout cl = (CardLayout) (cards.getLayout());
        cl.show(cards, "menu");
    }
}
