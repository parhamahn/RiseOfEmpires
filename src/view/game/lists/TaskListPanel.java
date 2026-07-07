package view.game.lists;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.Task;

public class TaskListPanel extends JPanel {

    public TaskListPanel() {
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

    public void clear() {
        removeAll();
        revalidate();
        repaint();
    }
}
