package view.game.map;

import controller.SelectionContext;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import java.awt.*;
import java.util.List;

public class BorderLayer implements MapLayer {

    private static final Color BORDER_COLOR = new Color(0, 31, 232);
    private static final int[] EDGE_START_FOR_DIRECTION = {0, 3, 1, 2, 5, 4};

    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        if (map == null) return;

        g2.setColor(BORDER_COLOR);
        g2.setStroke(new BasicStroke(Math.max(1f, camera.getHexRadius() / 20f),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null || !hex.isInEmpireBorder()) continue;

                Point center = camera.getHexCenter(c, r);
                Polygon tile = camera.translatePolygon(camera.getHexShape(), center.x, center.y);

                List<Hex> neighbours = map.getNeighbourHexes(hex);
                for (int dir = 0; dir < neighbours.size(); dir++) {
                    Hex neighbour = neighbours.get(dir);
                    if (neighbour != null && neighbour.isInEmpireBorder()) continue;
                    int v1 = EDGE_START_FOR_DIRECTION[dir];
                    int v2 = (v1 + 1) % 6;
                    g2.drawLine(tile.xpoints[v1], tile.ypoints[v1],
                            tile.xpoints[v2], tile.ypoints[v2]);
                }
            }
        }
    }
}
