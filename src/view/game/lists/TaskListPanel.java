package view.game.lists;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helpers.ImageLoader;
import model.Task;
import view.common.StyledButtons;

public class TaskListPanel extends JPanel {

    private JButton destroyButton;

    public JButton getDestroyButton() {
        return destroyButton;
    }

    public TaskListPanel() {
        Icon icon = ImageLoader.getInstance().loadIcon("/utils/destroybutton.png", 75);
        this.destroyButton = StyledButtons.iconButton(icon);
        StyledButtons.goldBorder(this.destroyButton);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));
    }


    public List<TaskStat> updateTasks(Map<Task.TASK, Boolean> canBuildMap) {
        removeAll();
        List<TaskStat> stats = new ArrayList<>();
        for (Map.Entry<Task.TASK, Boolean> entry : canBuildMap.entrySet()) {
            TaskStat stat = new TaskStat(entry.getKey(), entry.getValue());
            add(stat.getButton());
            add(Box.createVerticalStrut(5));
            stats.add(stat);
        }
        revalidate();
        repaint();
        return stats;
    }

    public void showDestroyButton() {
        add(Box.createVerticalGlue());
        add(this.destroyButton);
    }

    public void clear() {
        removeAll();
        revalidate();
        repaint();
    }
}
