package view.buildingpanel.townhall;

import model.queue.Queue;
import model.state.GameState;
import view.common.UITheme;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class QueueSection extends JPanel {

    private final GameState gameState;
    private final JPanel queuePanel;
    private final List<QueueStat> currentStats = new ArrayList<>();
    private Consumer<Queue> onCancel;

    public QueueSection(GameState gameState) {
        this.gameState = gameState;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel queueLabel = new JLabel("Queue");
        queueLabel.setFont(UITheme.GAME_FONT.deriveFont(32f));
        queueLabel.setForeground(UITheme.GOLD);
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

    public void setOnCancel(Consumer<Queue> onCancel) {
        this.onCancel = onCancel;
    }

    public List<QueueStat> setQueue(List<Queue> queues) {
        queuePanel.removeAll();
        currentStats.clear();
        int currentTurn = gameState.getWorldState().getTurnNumber();

        for (Queue q : queues) {
            QueueStat stat = new QueueStat(q, currentTurn);
            if (onCancel != null) {
                stat.getCancelButton().addActionListener(e -> onCancel.accept(q));
            }
            queuePanel.add(stat);
            queuePanel.add(Box.createRigidArea(new Dimension(0, 2)));
            currentStats.add(stat);
        }

        if (queues.isEmpty()) {
            JLabel emptyLabel = new JLabel("No active queues");
            emptyLabel.setFont(UITheme.GAME_FONT_SMALL);
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(CENTER_ALIGNMENT);
            queuePanel.add(emptyLabel);
        }

        queuePanel.revalidate();
        queuePanel.repaint();
        return new ArrayList<>(currentStats);
    }

    public List<QueueStat> getCurrentStats() {
        return new ArrayList<>(currentStats);
    }
}
