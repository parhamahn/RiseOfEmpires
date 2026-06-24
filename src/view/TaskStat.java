package view;

import helpers.ImageLoader;
import model.Task;

import javax.swing.*;
import java.awt.*;

public class TaskStat{

    private Task.TASK task;
    private JButton button;

    public TaskStat(Task.TASK task){
        this.task = task;
        this.button = new JButton();

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

    }

    public JButton getButton() {
        return button;
    }
}
