package view.game.map;

import controller.SelectionContext;
import helpers.image.ImageLoader;
import model.building.edge.EdgeComponent;
import model.building.edge.Road;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EdgeComponentLayer implements MapLayer {

    private static final int[] EDGE_START_FOR_DIRECTION = {0, 3, 1, 2, 5, 4};

    private final HashMap<String, BufferedImage> imageCache = new HashMap<>();

    private BufferedImage getImage(String imageName) {
        return imageCache.computeIfAbsent(imageName, path -> ImageLoader.getInstance().loadImage(path));
    }

    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        if (map == null) return;

        Set<EdgeComponent> allComponents = new HashSet<>();
        for (Hex hex : map.getAllHexes()) {
            allComponents.addAll(hex.getWallsWith());
            allComponents.addAll(hex.getRoadsTo());
            allComponents.addAll(hex.getRiversWith());
        }

        int thickness = Math.max(6, camera.getHexRadius() / 6);

        for (EdgeComponent component : allComponents) {
            BufferedImage image = getImage(component.getImageName());
            if (image == null) continue;

            int midX, midY;
            double angle;
            double length;

            if (component instanceof Road) {
                Point centerA = camera.getHexCenter(component.getHexA());
                Point centerB = camera.getHexCenter(component.getHexB());
                midX = (centerA.x + centerB.x) / 2;
                midY = (centerA.y + centerB.y) / 2;
                angle = Math.atan2(centerB.y - centerA.y, centerB.x - centerA.x);
                length = Math.hypot(centerB.x - centerA.x, centerB.y - centerA.y)/2;
            } else {
                Hex hexA = component.getHexA();
                List<Hex> neighbours = map.getNeighbourHexes(hexA);
                int dir = neighbours.indexOf(component.getHexB());
                if (dir < 0) continue;

                Point center = camera.getHexCenter(hexA);
                Polygon tile = camera.translatePolygon(camera.getHexShape(), center.x, center.y);

                int v1 = EDGE_START_FOR_DIRECTION[dir];
                int v2 = (v1 + 1) % 6;

                int x1 = tile.xpoints[v1], y1 = tile.ypoints[v1];
                int x2 = tile.xpoints[v2], y2 = tile.ypoints[v2];

                midX = (x1 + x2) / 2;
                midY = (y1 + y2) / 2;
                angle = Math.atan2(y2 - y1, x2 - x1);
                length = Math.hypot(x2 - x1, y2 - y1);
            }

            AffineTransform old = g2.getTransform();
            g2.translate(midX, midY);
            g2.rotate(angle);
            g2.drawImage(image, (int) (-length / 2), -thickness / 2, (int) length, thickness, null);
            g2.setTransform(old);
        }
    }
}
