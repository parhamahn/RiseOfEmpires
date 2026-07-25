package view.buildingpanel.trading;

import model.storage.ResourceType;

public class ToGetSection extends OptionSection {

    private final ResourceType[] types;

    public ToGetSection(ResourceType[] types) {
        super("You get :", buildIcons(types), 90, 64);
        this.types = types;
    }

    public ResourceType getSelectedResource() {
        int i = getSelectedIndex();
        return i >= 0 ? types[i] : null;
    }

    private static String[] buildIcons(ResourceType[] types) {
        String[] icons = new String[types.length];
        for (int i = 0; i < types.length; i++) {
            icons[i] = iconPath(types[i]);
        }
        return icons;
    }

    private static String iconPath(ResourceType type) {
        return switch (type) {
            case WOOD -> "/utils/wood.png";
            case STONE -> "/utils/stone.png";
            case IRON -> "/utils/iron.png";
            case FOOD -> "/utils/food.png";
        };
    }
}
