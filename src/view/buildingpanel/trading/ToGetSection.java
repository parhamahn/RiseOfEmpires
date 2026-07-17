package view.buildingpanel.trading;

import model.storage.ResourceType;

public class ToGetSection extends OptionSection {

    private static final String[] ICONS = {"/utils/wood.png", "/utils/stone.png", "/utils/iron.png", "/utils/food.png"};
    private static final ResourceType[] TYPES = {ResourceType.WOOD, ResourceType.STONE, ResourceType.IRON, ResourceType.FOOD};

    public ToGetSection() {
        super("You get :", ICONS, 90, 64);
    }

    public ResourceType getSelectedResource() {
        int i = getSelectedIndex();
        return i >= 0 ? TYPES[i] : null;
    }
}
