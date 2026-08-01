package view.game.map;

import controller.SelectionContext;
import helpers.image.ImageLoader;
import model.map.Hex;
import model.map.Map;
import model.building.Building;
import model.state.GameState;
import view.common.UITheme;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class BuildingLayer implements MapLayer {

    private final java.util.Map<String, BufferedImage> buildingImages = new HashMap<>();


    public BuildingLayer() {
        ImageLoader loader = ImageLoader.getInstance();
        buildingImages.put("townhall", loader.loadImage("/utils/townhall.png"));
        buildingImages.put("farmhex", loader.loadImage("/utils/farmhex.png"));
        buildingImages.put("lumbermillhex", loader.loadImage("/utils/lumbermillhex.png"));
        buildingImages.put("stoneminehex", loader.loadImage("/utils/stoneminehex.png"));
        buildingImages.put("ironminehex", loader.loadImage("/utils/ironminehex.png"));
        buildingImages.put("stablehex", loader.loadImage("/utils/stablehex.png"));
        buildingImages.put("townhex", loader.loadImage("/utils/townhex.png"));
        buildingImages.put("monumenthex", loader.loadImage("/utils/monumenthex.png"));
        buildingImages.put("bazaarhex", loader.loadImage("/utils/bazaarhex.png"));
        buildingImages.put("dockhex", loader.loadImage("/utils/dockhex.png"));
        buildingImages.put("tradingcenterhex", loader.loadImage("/utils/tradingcenterhex.png"));
        buildingImages.put("militarystablehex", loader.loadImage("/utils/militarystablehex.png"));
        buildingImages.put("farmertribe", loader.loadImage("/utils/farmertribe.png"));
        buildingImages.put("mountaintribe", loader.loadImage("/utils/mountaintribe.png"));
        buildingImages.put("warriortribe", loader.loadImage("/utils/warriortribe.png"));
        buildingImages.put("tradertribe", loader.loadImage("/utils/tradertribe.png"));
        buildingImages.put("beachtribe", loader.loadImage("/utils/beachtribe.png"));
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
