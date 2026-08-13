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

    public static void tribeWarning(String message){
        JOptionPane.showMessageDialog(null, message, "Tribe", JOptionPane.WARNING_MESSAGE);
    }

    public static void warDeclared(String tribeName){
        JOptionPane.showMessageDialog(null, "War has been declared with " + tribeName + "!",
                "War", JOptionPane.WARNING_MESSAGE);
    }

    public static void allianceFormed(String tribeName){
        JOptionPane.showMessageDialog(null, "You are now allied with " + tribeName + "!",
                "Alliance", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void peaceMade(String tribeName){
        JOptionPane.showMessageDialog(null, "Peace has been made with " + tribeName + ".",
                "Peace", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void tribeAttackedPlayer(String tribeName){
        JOptionPane.showMessageDialog(null, tribeName + " attacked your units!",
                "Tribe Attack", JOptionPane.WARNING_MESSAGE);
    }

    public static void tribeProducedUnit(String tribeName){
        JOptionPane.showMessageDialog(null, tribeName + " produced a new guard unit.",
                "Tribe", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void newTradeOrMission(String tribeName){
        JOptionPane.showMessageDialog(null, tribeName + " offers you a new trade or mission opportunity.",
                "Tribe", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void missionFailed(String tribeName){
        JOptionPane.showMessageDialog(null, "The mission from " + tribeName + " failed!",
                "Mission", JOptionPane.WARNING_MESSAGE);
    }

    public static void missionCompleted(String tribeName){
        JOptionPane.showMessageDialog(null, "The mission for " + tribeName + " has been completed successfully!",
                "Mission", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void bearAttack(){
        JOptionPane.showMessageDialog(null, "A bear attacked your lands!",
                "Bear Attack", JOptionPane.WARNING_MESSAGE);
    }

}
