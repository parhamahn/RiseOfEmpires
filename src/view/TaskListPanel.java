package view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import model.GameState;
import model.Task;
import model.unit.Builder;
import model.unit.Unit;

public class TaskListPanel extends JPanel {
    public TaskListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));
    }

    public List<TaskStat> updateTasks(List<Task.TASK> tasks, GameState gameState) {
        removeAll();
        List<TaskStat> stats = new ArrayList<>();
        for (Task.TASK task : tasks) {
            boolean enoughAP = false;
            for(Unit unit:gameState.getSelectedHex().getUnits()){
                if(unit instanceof Builder){
                    enoughAP = Task.getCostForTask(task).enoughAP(unit.getActionPoints());
                }
            }
            boolean canBuild = Task.getCostForTask(task).enoughResources(gameState.getStorage()) && enoughAP;
            TaskStat stat = new TaskStat(task, canBuild);
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