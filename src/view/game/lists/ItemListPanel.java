package view.game.lists;

import model.Task;
import model.item.ItemType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemListPanel extends JPanel {

    public ItemListPanel(){

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
    }

    public List<ItemStat> updateItems(Map<ItemType, String> blockReasonMap){
        removeAll();
        List<ItemStat> stats = new ArrayList<>();
        for (Map.Entry<ItemType, String> entry : blockReasonMap.entrySet()) {
            ItemStat stat = new ItemStat(entry.getKey(), entry.getValue());
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