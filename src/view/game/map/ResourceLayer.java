package view.game.map;

import controller.SelectionContext;
import helpers.image.ImageLoader;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ResourceLayer implements MapLayer {

    private final java.util.Map<Hex.ResourceType, BufferedImage> resourceIcons = new HashMap<>();
    private static final Color BADGE_BG = new Color(0, 0, 0, 160);
    private static final Color BADGE_BORDER = new Color(255, 255, 255, 90);
    private static final BasicStroke BADGE_STROKE = new BasicStroke(1.5f);

    public ResourceLayer() {
        ImageLoader loader = ImageLoader.getInstance();
        resourceIcons.put(Hex.ResourceType.WOOD, loader.loadImage("/utils/wood.png"));
        resourceIcons.put(Hex.ResourceType.STONE, loader.loadImage("/utils/stone.png"));
        resourceIcons.put(Hex.ResourceType.IRON, loader.loadImage("/utils/iron.png"));
        resourceIcons.put(Hex.ResourceType.WHEAT, loader.loadImage("/utils/wheat.png"));
        resourceIcons.put(Hex.ResourceType.RICE, loader.loadImage("/utils/rice.png"));
        resourceIcons.put(Hex.ResourceType.COW, loader.loadImage("/utils/cow.png"));
        resourceIcons.put(Hex.ResourceType.SHEEP, loader.loadImage("/utils/sheep.png"));
        resourceIcons.put(Hex.ResourceType.FISH, loader.loadImage("/utils/fish.png"));
    }


    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        if (map == null) return;

        int r2 = camera.getHexRadius();
        int iconSize = Math.max(10, r2 / 5);
        int badgeRadius = iconSize / 2 + 7;
        int spacing = badgeRadius * 2 + 4;
        java.util.UUID playerId = gameState.getLocalPlayerId();

        for (int r = camera.getVisibleStartRow(); r < camera.getVisibleEndRow(map.getRows()); r++) {
            for (int c = camera.getVisibleStartCol(); c < camera.getVisibleEndCol(map.getCols()); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null || !hex.isDiscoveredBy(playerId) || hex.getBuilding() != null) continue;

                HashMap<Hex.ResourceType, Integer> resources = hex.getResources();
                if (resources.isEmpty()) continue;

                Point center = camera.getHexCenter(c, r);
                int rowWidth = spacing * resources.size();
                int startX = center.x - rowWidth / 2 + spacing / 2;
                int y = center.y + r2 / 2 + badgeRadius;

                Object[] resourceKeys = resources.keySet().toArray();
                for (int i = 0; i < resources.size(); i++) {
                    BufferedImage icon = resourceIcons.get(resourceKeys[i]);
                    if (icon == null) continue;

                    int iconX = startX + i * spacing;
                    Polygon badge = camera.createMiniHex(iconX, y - 2 * iconSize, badgeRadius);

                    g2.setColor(BADGE_BG);
                    g2.fillPolygon(badge);
                    g2.setColor(BADGE_BORDER);
                    g2.setStroke(BADGE_STROKE);
                    g2.drawPolygon(badge);

                    g2.drawImage(icon, iconX - iconSize / 2, y - 5 * iconSize / 2, iconSize, iconSize, null);
                }
            }
        }
    }
}
