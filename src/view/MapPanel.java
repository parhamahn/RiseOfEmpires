package view;

import javax.swing.*;
import java.awt.*;
import model.Map;
import model.Hex;

public class MapPanel extends JPanel {
    private final int HEX_RADIUS = 100;
    private final double SQRT3 = Math.sqrt(3);
    private final int DX = (int) (SQRT3 * HEX_RADIUS);
    private final int DY = (int) (1.5 * HEX_RADIUS);

    private Map map;
    private int cameraX = 0;
    private int cameraY = 0;

    private final Polygon hexShape = new Polygon();

    public MapPanel(Map map) {
        setBackground(Color.BLACK);
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(-30 + 60 * i);
            int x = (int) (HEX_RADIUS * Math.cos(angle));
            int y = (int) (HEX_RADIUS * Math.sin(angle));
            hexShape.addPoint(x, y);
        }
        setMap(map);
        cameraX = map.getCols()*HEX_RADIUS/2 + HEX_RADIUS;
        cameraY = map.getRows()*HEX_RADIUS/2 - HEX_RADIUS;
    }

    public void setMap(Map map) {
        this.map = map;
        repaint();
    }

    public void moveCamera(int dx, int dy) {
        if((cameraX + dx <= map.getCols()*HEX_RADIUS+100)&&(cameraX + dx >= -map.getCols()*HEX_RADIUS/10)){cameraX += dx;}
        if((cameraY + dy <= map.getRows()*HEX_RADIUS-100)&&(cameraY + dy >= -map.getRows()*HEX_RADIUS/10)){cameraY += dy;}
        System.out.println("("+cameraX+", "+cameraY+")");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawHexTiles(g);
    }

    private void drawHexTiles(Graphics g){
        if (map == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null) continue;

                int px = (int) ((c + 0.5 * (r & 1)) * DX) - cameraX;
                int py = (int) (r * DY) - cameraY;
                if (px < -HEX_RADIUS * 2 || px > getWidth() + HEX_RADIUS * 2 ||
                        py < -HEX_RADIUS * 2 || py > getHeight() + HEX_RADIUS * 2) continue;

                Polygon tile = translatePolygon(hexShape, px, py);
                if (hex.isDiscovered()) {
                    g2.setColor(getTerrainColor(hex.getTerrainType()));
                    g2.fillPolygon(tile);
                    g2.setColor(Color.DARK_GRAY);
                    g2.drawPolygon(tile);
                }else{
                    g2.setColor(Color.BLACK);
                    g2.fillPolygon(tile);
                    g2.setColor(Color.WHITE);
                    g2.drawPolygon(tile);
                    g2.setFont(new Font("Pixel Game", Font.BOLD, 48));
                    g2.drawString("?", px-10, py+10);
                }

            }
        }
    }

    private Polygon translatePolygon(Polygon p, int dx, int dy) {
        Polygon result = new Polygon();
        for (int i = 0; i < p.npoints; i++) {
            result.addPoint(p.xpoints[i] + dx, p.ypoints[i] + dy);
        }
        return result;
    }

    private Color getTerrainColor(Hex.TerrainType terrain) {
        return switch (terrain) {
            case PLAIN   -> new Color(107, 142, 35);
            case FOREST  -> new Color(0, 100, 0);
            case MOUNTAIN-> new Color(160, 160, 160);
            case MEADOW  -> new Color(144, 238, 144);
        };
    }
}