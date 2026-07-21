package view.game.lists;

import helpers.ImageLoader;
import model.Task;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;

public class TaskStat{

    private Task.TASK task;
    private JButton button;
    private boolean canAfford;


    public TaskStat(Task.TASK task, boolean canAfford){
        this.task = task;
        this.button = new JButton();
        this.canAfford = canAfford;

        String iconPath = "/utils/"+task.toString().toLowerCase()+".png";
        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 75);

        button.setIcon(icon);
        UITheme.styleIconButton(button);

        if(!this.canAfford){
            button.setDisabledIcon(UITheme.createDisabledIcon(icon));
            button.setEnabled(false);
        }

    }


    public JButton getButton() {
        return button;
    }
    public Task.TASK getTask() {
        return task;
    }
}
