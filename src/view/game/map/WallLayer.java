package view.game.map;

import controller.SelectionContext;
import model.building.Wall;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

public class WallLayer implements MapLayer {

    private static final int[] EDGE_START_FOR_DIRECTION = {0, 3, 1, 2, 5, 4};

    private final BufferedImage wallImage;

    public WallLayer() {
        wallImage = loadImage("/utils/wall.png");
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
        if (map == null || wallImage == null) return;

        int thickness = Math.max(6, camera.getHexRadius() / 6);

        for (model.building.Wall wall : gameState.getEmpireState().getWalls()) {
            Hex hexA = wall.getHexA();
            List<Hex> neighbours = map.getNeighbourHexes(hexA);
            int dir = neighbours.indexOf(wall.getHexB());
            if (dir < 0) continue;

            Point center = camera.getHexCenter(hexA);
            Polygon tile = camera.translatePolygon(camera.getHexShape(), center.x, center.y);

            int v1 = EDGE_START_FOR_DIRECTION[dir];
            int v2 = (v1 + 1) % 6;

            int x1 = tile.xpoints[v1], y1 = tile.ypoints[v1];
            int x2 = tile.xpoints[v2], y2 = tile.ypoints[v2];

            int midX = (x1 + x2) / 2;
            int midY = (y1 + y2) / 2;
            double angle = Math.atan2(y2 - y1, x2 - x1);
            double edgeLength = Math.hypot(x2 - x1, y2 - y1);

            AffineTransform old = g2.getTransform();
            g2.translate(midX, midY);
            g2.rotate(angle);
            g2.drawImage(wallImage, (int) (-edgeLength / 2), -thickness / 2, (int) edgeLength, thickness, null);
            g2.setTransform(old);
        }
    }
}