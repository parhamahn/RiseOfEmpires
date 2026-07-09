package view.buildingpanel.townhall;

import model.queue.*;
import model.state.GameState;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QueueSection extends JPanel {

    private final GameState gameState;
    private final JPanel queuePanel;

    public QueueSection(GameState gameState) {
        this.gameState = gameState;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel queueLabel = new JLabel("Queue");
        queueLabel.setFont(UITheme.GAME_FONT.deriveFont(16f));
        queueLabel.setForeground(Color.WHITE);
        queueLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(queueLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));

        queuePanel = new JPanel();
        queuePanel.setOpaque(false);
        queuePanel.setLayout(new BoxLayout(queuePanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(queuePanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setAlignmentX(CENTER_ALIGNMENT);

        add(scrollPane);
    }

    public void setQueue(List<Queue> queues) {
        queuePanel.removeAll();
        int currentTurn = gameState.getWorldState().getTurnNumber();
        for (Queue q : queues) {
            int turns = q.getTurnsLeft(currentTurn);
            String typeLabel;
            switch (q.getQueueType()) {
                case COOK -> typeLabel = "Cooking " + ((CookQueue) q).getUnit().getUnitType().toString();
                case UPGRADE -> typeLabel = "Upgrading to " + ((UpgradeQueue) q).getTargetLevel();
                case RESEARCH -> typeLabel = "Researching " + ((ResearchQueue) q).getTechnology();
                default -> typeLabel = "Unknown";
            }

            String text = typeLabel + " : " + turns + " turn" + (turns != 1 ? "s" : "") + " left";

            JLabel label = new JLabel(text);
            label.setFont(UITheme.GAME_FONT_SMALL);
            label.setForeground(turns == 0 ? Color.GREEN : Color.WHITE);
            label.setAlignmentX(CENTER_ALIGNMENT);

            queuePanel.add(label);
            queuePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        }
        queuePanel.revalidate();
        queuePanel.repaint();
    }
}
