package view.common;

import javax.swing.*;

public class WarningDialogs {

    public static void upkeepDisaster(){
        JOptionPane.showMessageDialog(null, "You don't have enough resources to keep your building !");
    }

    public static void starvationDisaster(){
        JOptionPane.showMessageDialog(null, "You don't have enough food to feed your people !");
    }

    public static int unitsWithActionPoints(){
        return JOptionPane.showConfirmDialog(null, "There are units with action points. Are you sure ?","", JOptionPane.YES_NO_OPTION);
    }

    public static void autosaveFailed(String reason){
        JOptionPane.showMessageDialog(null,
                "Autosave failed: " + (reason == null ? "unknown error" : reason)
                        + "\nYour last autosave has been preserved.",
                "Autosave Error", JOptionPane.ERROR_MESSAGE);
    }

}
