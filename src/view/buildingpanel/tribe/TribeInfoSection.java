package view.buildingpanel.tribe;

import model.building.tribes.Tribe;
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


        add(tribeName);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
        add(tribeType);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
        add(relationValue);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
        add(hex);
        add(Box.createRigidArea(new Dimension(0, spaceSize)));
    }

    public void reset(Tribe tribe) {
        if (tribe == null) return;

        tribeName.setText("Name : " + tribe.getName());
        tribeType.setText("Type : " + tribe.getClass().getSimpleName().replace("Tribe", ""));

        int relation = tribe.getRelation();
        relationValue.setText("Relation :   " + relation + "       " + tribe.getRelationStatus().name());

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
