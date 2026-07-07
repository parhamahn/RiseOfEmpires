package view.game.map;

import controller.SelectionContext;
import model.Hex;
import model.Map;
import model.state.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;

public class TerrainLayer implements MapLayer {

    private final java.util.Map<Hex.TerrainType, BufferedImage> terrainImages = new EnumMap<>(Hex.TerrainType.class);


    public TerrainLayer() {
        terrainImages.put(Hex.TerrainType.PLAIN, loadImage("/utils/plainhex.png"));
        terrainImages.put(Hex.TerrainType.FOREST, loadImage("/utils/foresthex.png"));
        terrainImages.put(Hex.TerrainType.MOUNTAIN, loadImage("/utils/mountainhex.png"));
        terrainImages.put(Hex.TerrainType.MEADOW, loadImage("/utils/meadowhex.png"));
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
        if (map == null) return;

        int r2 = camera.getHexRadius();

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null) continue;

                Point center = camera.getHexCenter(c, r);
                Polygon tile = camera.translatePolygon(camera.getHexShape(), center.x, center.y);

                if (hex.isDiscovered() || !hex.getUnits().isEmpty()) {
                    BufferedImage img = terrainImages.get(hex.getTerrainType());
                    if (img == null) continue;
                    Shape oldClip = g2.getClip();
                    g2.setClip(tile);
                    g2.drawImage(img, center.x - r2, center.y - r2, r2 * 2, r2 * 2, null);
                    g2.setClip(oldClip);
                } else {
                    g2.setColor(Color.BLACK);
                    g2.fillPolygon(tile);
                    g2.setColor(Color.WHITE);
                    g2.drawPolygon(tile);
                    if (selection.getSelectedHex() != hex) {
                        g2.setFont(new Font("Pixel Game", Font.BOLD, Math.max(12, r2 / 2)));
                        g2.drawString("?", center.x - r2 / 10, center.y + r2 / 10);
                    }
                }
            }
        }
    }
}
