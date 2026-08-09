package view.buildingpanel.tribe;

import model.building.tribes.Tribe;
import model.storage.ResourceType;
import view.common.BaseSection;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;

public class TribeInfoSection extends BaseSection {

    public static float fontSize = 32f;
    public static int spaceSize = 50;


    private final JLabel tribeName;
    private final JLabel tribeType;
    private final JLabel relationValue;
    private final JLabel hex;
    private final JLabel missionSummary;
    private final JLabel tradeableResources;

    public TribeInfoSection() {
        super("Tribe's Info", 40);

        tribeName = new JLabel();
        tribeName.setFont(UITheme.GAME_FONT.deriveFont(fontSize));
        tribeName.setForeground(Color.WHITE);
        tribeName.setAlignmentX(Component.CENTER_ALIGNMENT);

        tribeType = new JLabel();
        tribeType.setFont(UITheme.GAME_FONT.deriveFont(fontSize));
        tribeType.setForeground(Color.WHITE);
        tribeType.setAlignmentX(Component.CENTER_ALIGNMENT);

        relationValue = new JLabel();
        relationValue.setFont(UITheme.GAME_FONT.deriveFont(fontSize));
        relationValue.setAlignmentX(Component.CENTER_ALIGNMENT);

        hex = new JLabel();
        hex.setFont(UITheme.GAME_FONT.deriveFont(fontSize));
        hex.setForeground(Color.WHITE);
        hex.setAlignmentX(Component.CENTER_ALIGNMENT);

        missionSummary = new JLabel();
        missionSummary.setFont(UITheme.GAME_FONT.deriveFont(fontSize));
        missionSummary.setAlignmentX(Component.CENTER_ALIGNMENT);
        missionSummary.setForeground(Color.WHITE);

        tradeableResources = new JLabel();
        tradeableResources.setFont(UITheme.GAME_FONT.deriveFont(fontSize));
        tradeableResources.setAlignmentX(Component.CENTER_ALIGNMENT);
        tradeableResources.setForeground(Color.WHITE);


        add(tribeName);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
        add(tribeType);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
        add(relationValue);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
        add(hex);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
        add(missionSummary);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
        add(tradeableResources);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
    }

    public void reset(Tribe tribe) {
        if (tribe == null) return;

        tribeName.setText("Name : " + tribe.getName());
        tribeType.setText("Type : " + tribe.getClass().getSimpleName().replace("Tribe", ""));

        int relation = tribe.getRelation();
        relationValue.setText("Relation :   " + relation + "       " + tribe.getRelationStatus().name());

        if(tribe.getActiveMission()!=null){
            String summary = tribe.getActiveMission().getSummary();
            if(tribe.getActiveMission() instanceof model.mission.WarriorMission){
                summary += "  (Kills: " + ((model.mission.WarriorMission) tribe.getActiveMission()).getKilledEnemies() + "/3)";
            }
            summary += "  |  Deadline: " + tribe.getActiveMission().getDeadline() + " turns";
            missionSummary.setText(summary);
        }else{
            missionSummary.setText("No active mission");
        }

        String tradeables = "Trades : ";
        if(tribe.getTradeStrategy()!=null){
            for(ResourceType resourceType:tribe.getTradeStrategy().getResourceTypes()){
                tradeables += resourceType.name()+" ";
            }
        }else{
            tradeables += "Nothing";
        }
        tradeableResources.setText(tradeables);

        if (relation > 0) {
            relationValue.setForeground(UITheme.POSITIVE_CHANGE);
        } else if (relation < 0) {
            relationValue.setForeground(UITheme.NEGATIVE_CHANGE);
        } else {
            relationValue.setForeground(UITheme.ZERO_CHANGE);
        }

        hex.setText("Hex : (  " + tribe.getHex().getC() + " ,  "+tribe.getHex().getR() + "  )");

    }
}
