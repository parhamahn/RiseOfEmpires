package view.game.map;

import model.Hex;
import model.Map;

import java.awt.*;

public class Camera {

    private static final double SQRT3 = Math.sqrt(3);
    private static final int MIN_RADIUS = 40;
    private static final int MAX_RADIUS = 180;
    private static final int ZOOM_STEP = 15;

    private int hexRadius = 100;
    private int dx;
    private int dy;
    private int cameraX = 0;
    private int cameraY = 0;
    private final Polygon hexShape = new Polygon();


    public Camera() {
        rebuildHexShape();
    }


    public int getHexRadius() { return hexRadius; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }
    public Polygon getHexShape() { return hexShape; }


    public void rebuildHexShape() {
        dx = (int) (SQRT3 * hexRadius);
        dy = (int) (1.5 * hexRadius);
        hexShape.reset();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(-30 + 60 * i);
            hexShape.addPoint((int) (hexRadius * Math.cos(angle)), (int) (hexRadius * Math.sin(angle)));
        }
    }

    public Point getHexCenter(int c, int r) {
        return new Point(
                (int) ((c + 0.5 * (r & 1)) * dx) - cameraX,
                r * dy - cameraY
        );
    }

    public Point getHexCenter(Hex hex) {
        return getHexCenter(hex.getC(), hex.getR());
    }

    public Hex getHexAt(int screenX, int screenY, Map map) {
        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Point center = getHexCenter(c, r);
                Polygon tile = translatePolygon(hexShape, center.x, center.y);
                if (tile.contains(screenX, screenY)) {
                    return map.getHex(c, r);
                }
            }
        }
        return null;
    }

    public boolean zoom(int direction, int mapWorldW, int mapWorldH, int viewportW, int viewportH) {
        int newRadius = hexRadius + direction * ZOOM_STEP;
        if (newRadius < MIN_RADIUS || newRadius > MAX_RADIUS) return false;

        int cx = viewportW / 2;
        int cy = viewportH / 2;

        int newDx = (int) (SQRT3 * newRadius);
        int newDy = (int) (1.5 * newRadius);

        cameraX = (int) ((cameraX + cx) * (double) newDx / dx) - cx;
        cameraY = (int) ((cameraY + cy) * (double) newDy / dy) - cy;

        hexRadius = newRadius;
        rebuildHexShape();

        clampCamera(mapWorldW, mapWorldH, viewportW, viewportH);
        return true;
    }

    public void moveCamera(int deltaX, int deltaY, int mapWorldW, int mapWorldH, int viewportW, int viewportH) {
        cameraX += deltaX;
        cameraY += deltaY;
        clampCamera(mapWorldW, mapWorldH, viewportW, viewportH);
    }

    public void centerOn(int worldX, int worldY, int viewportW, int viewportH) {
        cameraX = worldX - viewportW / 2;
        cameraY = worldY - viewportH / 2;
    }

    public Polygon translatePolygon(Polygon p, int offsetX, int offsetY) {
        Polygon result = new Polygon();
        for (int i = 0; i < p.npoints; i++) {
            result.addPoint(p.xpoints[i] + offsetX, p.ypoints[i] + offsetY);
        }
        return result;
    }

    public Polygon createMiniHex(int cx, int cy, int radius) {
        Polygon p = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(-30 + 60 * i);
            p.addPoint(cx + (int) (radius * Math.cos(angle)), cy + (int) (radius * Math.sin(angle)));
        }
        return p;
    }

    private void clampCamera(int mapWorldW, int mapWorldH, int viewportW, int viewportH) {
        cameraX = Math.max(-viewportW + dx, Math.min(mapWorldW - dx, cameraX));
        cameraY = Math.max(-viewportH + dy, Math.min(mapWorldH - dy, cameraY));
    }
}
