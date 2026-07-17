package view.buildingpanel.trading;

import model.storage.ResourceType;

public class ToGiveSection extends OptionSection {

    private static final String[] ICONS = {"/utils/wood.png", "/utils/stone.png", "/utils/iron.png", "/utils/food.png"};
    private static final ResourceType[] TYPES = {ResourceType.WOOD, ResourceType.STONE, ResourceType.IRON, ResourceType.FOOD};

    public ToGiveSection() {
        super("You give :", ICONS, 90, 64);
    }

    public ResourceType getSelectedResource() {
        int i = getSelectedIndex();
        return i >= 0 ? TYPES[i] : null;
    }
}
