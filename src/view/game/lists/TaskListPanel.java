package view.game.lists;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helpers.image.ImageLoader;
import model.Task;
import view.common.StyledButtons;

public class TaskListPanel extends JPanel {

    private JButton destroyButton;
    private JButton wallDestroyButton;
    private JButton roadDestroyButton;

    public JButton getDestroyButton() {
        return destroyButton;
    }
    public JButton getWallDestroyButton() {
        return wallDestroyButton;
    }
    public JButton getRoadDestroyButton() {
        return roadDestroyButton;
    }

    public TaskListPanel() {
        Icon icon = ImageLoader.getInstance().loadIcon("/utils/destroybutton.png", 75);
        this.destroyButton = StyledButtons.iconButton(icon);
        StyledButtons.goldBorder(this.destroyButton);

        Icon wallDestroyIcon = ImageLoader.getInstance().loadIcon("/utils/destroy_wall.png", 75);
        this.wallDestroyButton = StyledButtons.iconButton(wallDestroyIcon);
        StyledButtons.goldBorder(this.wallDestroyButton);

        Icon roadDestroyIcon = ImageLoader.getInstance().loadIcon("/utils/destroy_road.png", 75);
        this.roadDestroyButton = StyledButtons.iconButton(roadDestroyIcon);
        StyledButtons.goldBorder(this.roadDestroyButton);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));
    }


    public List<TaskStat> updateTasks(Map<Task.TASK, String> blockReasonMap) {
        removeAll();
        List<TaskStat> stats = new ArrayList<>();
        for (Map.Entry<Task.TASK, String> entry : blockReasonMap.entrySet()) {
            TaskStat stat = new TaskStat(entry.getKey(), entry.getValue());
            add(stat.getButton());
            add(Box.createVerticalStrut(5));
            stats.add(stat);
        }
        add(Box.createVerticalGlue());
        revalidate();
        repaint();
        return stats;
    }

    public void showDestroyButton() {
        add(this.destroyButton);
    }

    public void showWallDestroyButton() {
        add(this.wallDestroyButton);
    }

    public void showRoadDestroyButton() {
        add(this.roadDestroyButton);
    }

    public void clear() {
        removeAll();
        revalidate();
        repaint();
    }
}
