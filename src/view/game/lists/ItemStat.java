package view.game.lists;

import helpers.image.ImageLoader;
import model.item.ItemType;
import view.common.StyledButtons;
import view.common.UITheme;

import javax.swing.*;

public class ItemStat {

    private JButton button;
    private ItemType itemType;

    public ItemStat(ItemType itemType, String blockReason){
        this.itemType = itemType;

        String iconPath = "/utils/"+itemType.toString().toLowerCase()+".png";
        ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, 50);

        button = new JButton();

        button.setIcon(icon);
        UITheme.styleIconButton(button);
        StyledButtons.goldBorder(button);

        if(blockReason != null){
            button.setDisabledIcon(UITheme.createDisabledIcon(icon));
            StyledButtons.setDisabled(button, true, blockReason);
        }

    }

    public JButton getButton() {
        return button;
    }

    public ItemType getItemType() {
        return itemType;
    }
}
