package view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import model.Task;

public class TaskListPanel extends JPanel {
    public TaskListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));
    }

    public List<TaskStat> updateTasks(List<Task.TASK> tasks) {
        removeAll();
        List<TaskStat> stats = new ArrayList<>();
        for (Task.TASK task : tasks) {
            TaskStat stat = new TaskStat(task);
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