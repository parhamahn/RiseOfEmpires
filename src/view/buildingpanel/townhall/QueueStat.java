package view.buildingpanel.townhall;

import helpers.image.ImageLoader;
import model.queue.*;
import view.common.FormatUtils;
import view.common.StyledButtons;
import view.common.UITheme;
import view.game.GameAssets;

import javax.swing.*;
import java.awt.*;

public class QueueStat extends JPanel {

    private final JButton cancelButton;
    private Queue queue;

    public QueueStat(Queue queue, int currentTurn) {
        this.queue = queue;
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPathFor(queue), 40);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentY(CENTER_ALIGNMENT);
        add(iconLabel);
        add(Box.createRigidArea(new Dimension(10, 0)));

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setAlignmentY(CENTER_ALIGNMENT);

        JLabel descLabel = new JLabel(descriptionFor(queue));
        descLabel.setFont(UITheme.GAME_FONT.deriveFont(15f));
        descLabel.setForeground(Color.WHITE);
        textPanel.add(descLabel);

        int turns = queue.getTurnsLeft(currentTurn);
        JLabel turnsLabel = new JLabel(turns + " turn" + (turns != 1 ? "s" : "") + " left");
        turnsLabel.setFont(UITheme.GAME_FONT_SMALL);
        turnsLabel.setForeground(turns == 0 ? Color.GREEN : Color.LIGHT_GRAY);
        textPanel.add(turnsLabel);

        add(textPanel);
        add(Box.createHorizontalGlue());

        Icon cancelIcon = ImageLoader.getInstance().loadScaledIcon("/utils/menubutton.png", 100, 40);
        cancelButton = StyledButtons.textButton("Cancel", cancelIcon, new Dimension(100, 40), 20f, UITheme.MENU_BUTTON_FG);
        add(cancelButton);
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    private String descriptionFor(Queue queue) {
        return switch (queue.getQueueType()) {
            case COOK -> "Cooking " + FormatUtils.formatEnum(((CookQueue) queue).getUnit().getUnitType().toString());
            case UPGRADE -> "Upgrading to " + FormatUtils.formatEnum(((UpgradeQueue) queue).getTargetLevel().toString());
            case RESEARCH -> "Researching " + FormatUtils.formatEnum(((ResearchQueue) queue).getTechnology().toString());
        };
    }

    private String iconPathFor(Queue queue) {
        return switch (queue.getQueueType()) {
            case COOK -> "/utils/" + GameAssets.unitImage(((CookQueue) queue).getUnit()) + ".png";
            case UPGRADE -> "/utils/townhall.png";
            case RESEARCH -> switch (((ResearchQueue) queue).getTechnology()) {
                case SAILING -> "/utils/sailingresearch.png";
                case STEEL_TOOLS -> "/utils/steelresearch.png";
                case DEFENSIVE_PATTERN -> "/utils/defensivepatternresearch.png";
            };
        };
    }

    public Queue getQueue() {
        return queue;
    }
}
