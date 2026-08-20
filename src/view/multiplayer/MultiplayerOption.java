package view.multiplayer;

import javax.swing.*;

public class MultiplayerOption {

    public static int hostOrJoin(){
        Object[] choices = {"Host", "Join"};
        Object defaultChoice = choices[0];
        return JOptionPane.showOptionDialog(null, "Do you want to host or join a lobby ?", "Multiplayer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null, choices, defaultChoice);
    }

    public static String joinAddress(){
        return JOptionPane.showInputDialog(null, "Enter IP address of server", "Join", JOptionPane.PLAIN_MESSAGE);
    }

}
