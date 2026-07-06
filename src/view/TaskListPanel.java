package view;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import controller.SelectionContext;
import model.state.GameState;
import model.Task;
import model.building.Building;
import model.unit.Unit;

public class TaskListPanel extends JPanel {
    public TaskListPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(75, 10, 10, 10));
    }

    public List<TaskStat> updateTasks(List<Task.TASK> tasks, GameState gameState, SelectionContext selectionContext) {
        removeAll();
        List<TaskStat> stats = new ArrayList<>();
        for (Task.TASK task : tasks) {
            boolean enoughAP = false;
            for(Unit unit: selectionContext.getSelectedHex().getUnits()){
                if(unit.isBuilder()){
                    enoughAP = Building.getBuildCost(task).enoughAP(unit.getActionPoints());
                }
            }
            boolean canBuild = Building.getBuildCost(task).enoughResources(gameState.getEmpireState().getStorage()) && enoughAP && selectionContext.getSelectedHex().isInEmpireBorder();
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
