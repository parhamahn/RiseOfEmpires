package view;

import helpers.ImageLoader;
import model.Task;

import javax.swing.*;
import java.awt.*;

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
        button.setFocusPainted(false);
        button.setBackground(new Color(0,0,0,0));
        button.setMargin(new Insets(0,0,0,0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        if(!canAfford){
            Image grayImage = GrayFilter.createDisabledImage(icon.getImage());
            button.setDisabledIcon(new ImageIcon(grayImage));
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
