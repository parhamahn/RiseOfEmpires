package view;

import javax.swing.*;
import java.util.List;

import model.Task;

public class TaskListPanel extends JPanel {
    public TaskListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));
    }

    public void updateTasks(List<Task.TASK> tasks) {
        removeAll();
        for (Task.TASK task : tasks) {
            TaskStat stat = new TaskStat(task);
            add(stat.getButton());
            add(Box.createVerticalStrut(5));
        }
        revalidate();
        repaint();
    }

    public void clear() {
        removeAll();
        revalidate();
        repaint();
    }
}