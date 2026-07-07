package view.game.map;

import controller.SelectionContext;
import model.Map;
import model.building.Building;
import model.state.GameState;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class BuildingLayer implements MapLayer {

    private final java.util.Map<String, BufferedImage> buildingImages = new HashMap<>();


    public BuildingLayer() {
        buildingImages.put("townhall", loadImage("/utils/townhall.png"));
        buildingImages.put("farmhex", loadImage("/utils/farmhex.png"));
        buildingImages.put("lumbermillhex", loadImage("/utils/lumbermillhex.png"));
        buildingImages.put("stoneminehex", loadImage("/utils/stoneminehex.png"));
        buildingImages.put("ironminehex", loadImage("/utils/ironminehex.png"));
        buildingImages.put("stablehex", loadImage("/utils/stablehex.png"));
        buildingImages.put("townhex", loadImage("/utils/townhex.png"));
    }


    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        int r2 = camera.getHexRadius();

        for (Building building : gameState.getEmpireState().getBuildings()) {
            BufferedImage img = buildingImages.get(building.getBuildingImageName());
            if (img == null) continue;

            Point center = camera.getHexCenter(building.getHex());
            Polygon tile = camera.translatePolygon(camera.getHexShape(), center.x, center.y);

            Shape oldClip = g2.getClip();
            g2.setClip(tile);
            g2.drawImage(img, center.x - r2, center.y - r2, r2 * 2, r2 * 2, null);
            g2.setClip(oldClip);
        }
    }
}
