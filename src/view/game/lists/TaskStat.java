package view.game.lists;

import helpers.image.ImageLoader;
import model.Task;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;

public class TaskStat{

    private Task.TASK task;
    private JButton button;


    public TaskStat(Task.TASK task, String blockReason){
        this.task = task;
        this.button = new JButton();

        String iconPath = "/utils/"+task.toString().toLowerCase()+".png";
        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 75);

        button.setIcon(icon);
        UITheme.styleIconButton(button);

        if(blockReason != null){
            button.setDisabledIcon(UITheme.createDisabledIcon(icon));
            StyledButtons.setDisabled(button, true, blockReason);
        }

    }


    public JButton getButton() {
        return button;
    }
    public Task.TASK getTask() {
        return task;
    }
}
