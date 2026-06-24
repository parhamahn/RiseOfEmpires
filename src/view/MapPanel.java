package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import model.*;

public class MapPanel extends JPanel {
    private final int HEX_RADIUS = 100;
    private final double SQRT3 = Math.sqrt(3);
    private final int DX = (int) (SQRT3 * HEX_RADIUS);
    private final int DY = (int) (1.5 * HEX_RADIUS);
    private final java.util.Map<Hex.ResourceType, BufferedImage> resourceIcons = new java.util.HashMap<>();

    private Map map;
    private GameState gameState;
    private int cameraX = 0;
    private int cameraY = 0;
    private BufferedImage plainImage;
    private BufferedImage forestImage;
    private BufferedImage mountainImage;
    private BufferedImage meadowImage;
    private BufferedImage townHallImage;

    private final Polygon hexShape = new Polygon();

    public MapPanel(Map map, GameState gameState) {
        this.gameState = gameState;
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
        try {
            plainImage = ImageIO.read(getClass().getResource("/utils/plainhex.png"));
            forestImage = ImageIO.read(getClass().getResource("/utils/foresthex.png"));
            mountainImage = ImageIO.read(getClass().getResource("/utils/mountainhex.png"));
            meadowImage = ImageIO.read(getClass().getResource("/utils/meadowhex.png"));
            townHallImage = ImageIO.read(getClass().getResource("/utils/townhall.png"));
            resourceIcons.put(Hex.ResourceType.WOOD, ImageIO.read(getClass().getResource("/utils/wood.png")));
            resourceIcons.put(Hex.ResourceType.STONE, ImageIO.read(getClass().getResource("/utils/stone.png")));
            resourceIcons.put(Hex.ResourceType.IRON, ImageIO.read(getClass().getResource("/utils/iron.png")));
            resourceIcons.put(Hex.ResourceType.WHEAT, ImageIO.read(getClass().getResource("/utils/wheat.png")));
            resourceIcons.put(Hex.ResourceType.RICE, ImageIO.read(getClass().getResource("/utils/rice.png")));
            resourceIcons.put(Hex.ResourceType.COW, ImageIO.read(getClass().getResource("/utils/cow.png")));
            resourceIcons.put(Hex.ResourceType.SHEEP, ImageIO.read(getClass().getResource("/utils/sheep.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    protected void paintComponent(Graphics g) {


        super.paintComponent(g);
        drawHexTiles(g);
        drawBuildings(g);
        drawHexHighlight(g);
        drawRawResources(g);


    }



    public void setMap(Map map) {
        this.map = map;
        repaint();
    }

    public void moveCamera(int dx, int dy) {
        if((cameraX + dx <= map.getCols()*HEX_RADIUS+100)&&(cameraX + dx >= -map.getCols()*HEX_RADIUS/10)){cameraX += dx;}
        if((cameraY + dy <= map.getRows()*HEX_RADIUS-100)&&(cameraY + dy >= -map.getRows()*HEX_RADIUS/10)){cameraY += dy;}
    }


    public Hex getHexAt(int screenX, int screenY) {
        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                int px = (int) ((c + 0.5 * (r & 1)) * DX) - cameraX;
                int py = (int) (r * DY) - cameraY;
                Polygon tile = translatePolygon(hexShape, px, py);
                if (tile.contains(screenX, screenY)) {
                    return map.getHex(c, r);
                }
            }
        }
        return null;
    }


    private void drawHexTiles(Graphics g){
        if (map == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null) continue;

                Point center = getHexCenter(c, r);

                Polygon tile = translatePolygon(hexShape, center.x, center.y);
                if (hex.isDiscovered()) {
                    BufferedImage img = getTerrainImage(hex.getTerrainType());
                    g2.drawImage(
                            img,
                            center.x - HEX_RADIUS + 12,
                            center.y - HEX_RADIUS,
                            HEX_RADIUS * 2 - 20,
                            HEX_RADIUS * 2,
                            null
                    );
                }else{
                    g2.setColor(Color.BLACK);
                    g2.fillPolygon(tile);
                    g2.setColor(Color.WHITE);
                    g2.drawPolygon(tile);
                    if (gameState.getSelectedHex()!=hex) {
                        g2.setFont(new Font("Pixel Game", Font.BOLD, 48));
                        g2.drawString("?", center.x - 10, center.y + 10);
                    }
                }

            }
        }
    }

    private Point getHexCenter(int c, int r) {
        int px = (int) ((c + 0.5 * (r & 1)) * DX) - cameraX;
        int py = (int) (r * DY) - cameraY;
        return new Point(px, py);
    }

    private void drawBuildings(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int imgWidth = HEX_RADIUS*7/4;
        int imgHeight = HEX_RADIUS*7/3;

        for (Building building : gameState.getBuildings()) {
            BufferedImage img = getBuildingImage(building);
            if (img == null) continue;

            Point center = getHexCenter(building.getHex().getC(), building.getHex().getR());
            g2.drawImage(img,
                    center.x - imgWidth / 2,
                    center.y - imgHeight / 2 - 20,
                    imgWidth,
                    imgHeight,
                    null);
        }
    }

    private void drawHexHighlight(Graphics g){
        if(gameState.getSelectedHex()!=null && gameState.getSelectedHex().getBuilding()==null){
            Graphics2D g2 = (Graphics2D) g;
            int c = gameState.getSelectedHex().getC();
            int r = gameState.getSelectedHex().getR();
            int px = (int) ((c + 0.5 * (r & 1)) * DX) - cameraX;
            int py = (int) (r * DY) - cameraY;
            Polygon tile = translatePolygon(hexShape, px, py);
            g2.setColor(new Color(238, 255, 0));
            g2.setStroke(new BasicStroke(4));
            g2.drawPolygon(tile);
        }
    }


    private Polygon translatePolygon(Polygon p, int dx, int dy) {
        Polygon result = new Polygon();
        for (int i = 0; i < p.npoints; i++) {
            result.addPoint(p.xpoints[i] + dx, p.ypoints[i] + dy);
        }
        return result;
    }

    private BufferedImage getTerrainImage(Hex.TerrainType terrain) {
        return switch (terrain) {
            case PLAIN -> plainImage;
            case FOREST -> forestImage;
            case MOUNTAIN -> mountainImage;
            case MEADOW -> meadowImage;
        };
    }

    private Polygon createMiniHex(int cx, int cy, int radius) {
        Polygon p = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(-30 + 60 * i);
            int x = cx + (int) (radius * Math.cos(angle));
            int y = cy + (int) (radius * Math.sin(angle));
            p.addPoint(x, y);
        }
        return p;
    }

    private void drawRawResources(Graphics g) {
        if (map == null) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int iconSize = 22;
        int badgeRadius = iconSize / 2 + 7;
        int spacing = badgeRadius * 2 + 4;

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null || !hex.isDiscovered() || hex.getBuilding() != null) continue;

                java.util.List<Hex.ResourceType> resources = hex.getResourceTypes();
                if (resources.isEmpty()) continue;

                Point center = getHexCenter(c, r);
                int rowWidth = spacing * resources.size();
                int startX = center.x - rowWidth / 2 + spacing / 2;
                int y = center.y + HEX_RADIUS / 2 + badgeRadius;

                for (int i = 0; i < resources.size(); i++) {
                    BufferedImage icon = resourceIcons.get(resources.get(i));
                    if (icon == null) continue;

                    int cx = startX + i * spacing;
                    Polygon badge = createMiniHex(cx, y-2*iconSize, badgeRadius);

                    g2.setColor(new Color(0, 0, 0, 160));
                    g2.fillPolygon(badge);
                    g2.setColor(new Color(255, 255, 255, 90));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawPolygon(badge);

                    g2.drawImage(icon, cx - iconSize / 2, y - 5*iconSize/2, iconSize, iconSize, null);
                }
            }
        }
    }


    private BufferedImage getBuildingImage(Building building) {
        if(building instanceof TownHall){
            return townHallImage;
        }else{
            return null;
        }
    }
}