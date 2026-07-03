package view;

import javax.swing.*;

public class WarningDialogs {

    public static void upkeepDisaster(JPanel jPanel){
        JOptionPane.showMessageDialog(jPanel, "You don't have enough resources to keep your building !");
    }

    public static void starvationDisaster(JPanel jPanel){
        JOptionPane.showMessageDialog(jPanel, "You don't have enough food to feed your people !");
    }

    public static int unitsWithActionPoints(JPanel jPanel){
        return JOptionPane.showConfirmDialog(jPanel, "There are units with action points. Are you sure ?","", JOptionPane.YES_NO_OPTION);
    }

}
