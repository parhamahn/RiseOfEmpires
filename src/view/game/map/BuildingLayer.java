package view.game.map;

import controller.SelectionContext;
import model.map.Hex;
import model.map.Map;
import model.building.Building;
import model.state.GameState;
import view.common.UITheme;

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
        buildingImages.put("monumenthex", loadImage("/utils/monumenthex.png"));
        buildingImages.put("bazaarhex", loadImage("/utils/bazaarhex.png"));
        buildingImages.put("dockhex", loadImage("/utils/dockhex.png"));
        buildingImages.put("tradingcenterhex", loadImage("/utils/tradingcenterhex.png"));
        buildingImages.put("militarystablehex", loadImage("/utils/militarystablehex.png"));
        buildingImages.put("farmertribe", loadImage("/utils/farmertribe.png"));
        buildingImages.put("mountaintribe", loadImage("/utils/mountaintribe.png"));
        buildingImages.put("warriortribe", loadImage("/utils/warriortribe.png"));
        buildingImages.put("tradertribe", loadImage("/utils/tradertribe.png"));
        buildingImages.put("beachtribe", loadImage("/utils/beachtribe.png"));
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

        for (int r = camera.getVisibleStartRow(); r < camera.getVisibleEndRow(map.getRows()); r++) {
            for (int c = camera.getVisibleStartCol(); c < camera.getVisibleEndCol(map.getCols()); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null) continue;
                Building building = hex.getBuilding();
                if (building == null) continue;

                BufferedImage img = buildingImages.get(building.getBuildingImageName());
                if (img == null) continue;

                Point center = camera.getHexCenter(hex);
                Polygon tile = camera.translatePolygon(camera.getHexShape(), center.x, center.y);

                double hpf = (double) building.getHP() / building.getMaxHP();
                int barWidth = camera.getHexRadius() / 4 * 3;
                int barHeight = 4;
                int barX = center.x - barWidth / 2;
                int barY = center.y + barWidth / 2;

                Shape oldClip = g2.getClip();
                g2.setClip(tile);
                g2.drawImage(img, center.x - r2, center.y - r2, r2 * 2, r2 * 2, null);
                g2.setClip(oldClip);
                if (hpf != 1) {
                    UITheme.drawHPBar(g2, barX, barY, barWidth, barHeight, hpf);
                }
            }
        }
    }
}
