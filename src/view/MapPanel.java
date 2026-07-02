package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import helpers.ImageLoader;
import model.*;
import model.building.*;
import model.unit.Unit;

public class MapPanel extends JPanel {
    private static final double SQRT3 = Math.sqrt(3);
    private static final int MIN_RADIUS = 40;
    private static final int MAX_RADIUS = 180;
    private static final int ZOOM_STEP = 15;

    private int hexRadius = 100;
    private int dx;
    private int dy;
    private final java.util.Map<Hex.ResourceType, BufferedImage> resourceIcons = new java.util.HashMap<>();

    private Runnable onUnitArrived;
    private Map map;
    private GameState gameState;
    private int cameraX = 0;
    private int cameraY = 0;
    private BufferedImage plainImage;
    private BufferedImage forestImage;
    private BufferedImage mountainImage;
    private BufferedImage meadowImage;
    private BufferedImage townHallImage;
    private BufferedImage farmHexImage;
    private BufferedImage stoneMineHexImage;
    private BufferedImage ironMineHexImage;
    private BufferedImage stableHexImage;
    private BufferedImage townHexImage;
    private BufferedImage lumberMillHexImage;

    private final Polygon hexShape = new Polygon();
    private static final int[] EDGE_START_FOR_DIRECTION = {0, 3, 1, 2, 5, 4};

    public MapPanel(Map map, GameState gameState) {
        this.gameState = gameState;
        setBackground(Color.BLACK);
        rebuildHexShape();
        setMap(map);
        cameraX = map.getCols() * hexRadius / 2 + hexRadius;
        cameraY = map.getRows() * hexRadius / 2 - hexRadius;
        try {
            plainImage = ImageIO.read(getClass().getResource("/utils/plainhex.png"));
            forestImage = ImageIO.read(getClass().getResource("/utils/foresthex.png"));
            mountainImage = ImageIO.read(getClass().getResource("/utils/mountainhex.png"));
            meadowImage = ImageIO.read(getClass().getResource("/utils/meadowhex.png"));
            townHallImage = ImageIO.read(getClass().getResource("/utils/townhall.png"));
            farmHexImage = ImageIO.read(getClass().getResource("/utils/farmhex.png"));
            lumberMillHexImage = ImageIO.read(getClass().getResource("/utils/lumbermillhex.png"));
            townHexImage = ImageIO.read(getClass().getResource("/utils/townhex.png"));
            stoneMineHexImage = ImageIO.read(getClass().getResource("/utils/stoneminehex.png"));
            ironMineHexImage = ImageIO.read(getClass().getResource("/utils/ironminehex.png"));
            stableHexImage = ImageIO.read(getClass().getResource("/utils/stablehex.png"));

            resourceIcons.put(Hex.ResourceType.WOOD,  ImageIO.read(getClass().getResource("/utils/wood.png")));
            resourceIcons.put(Hex.ResourceType.STONE, ImageIO.read(getClass().getResource("/utils/stone.png")));
            resourceIcons.put(Hex.ResourceType.IRON,  ImageIO.read(getClass().getResource("/utils/iron.png")));
            resourceIcons.put(Hex.ResourceType.WHEAT, ImageIO.read(getClass().getResource("/utils/wheat.png")));
            resourceIcons.put(Hex.ResourceType.RICE,  ImageIO.read(getClass().getResource("/utils/rice.png")));
            resourceIcons.put(Hex.ResourceType.COW,   ImageIO.read(getClass().getResource("/utils/cow.png")));
            resourceIcons.put(Hex.ResourceType.SHEEP, ImageIO.read(getClass().getResource("/utils/sheep.png")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        initUnitPositions();
    }

    private void rebuildHexShape() {
        dx = (int) (SQRT3 * hexRadius);
        dy = (int) (1.5 * hexRadius);
        hexShape.reset();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(-30 + 60 * i);
            int x = (int) (hexRadius * Math.cos(angle));
            int y = (int) (hexRadius * Math.sin(angle));
            hexShape.addPoint(x, y);
        }
    }

    private void initUnitPositions() {
        for (Unit unit : gameState.getUnits()) {
            if (!unit.isMoving()) {
                Point p = getHexCenter(unit.getHex());
                unit.setPositionX(p.x);
                unit.setPositionY(p.y);
            } else {
                animateUnitTowardHex(unit);
            }
        }
    }

    private void animateUnitTowardHex(Unit unit) {
        Point target = getHexCenter(unit.getTargetHex());
        int dx = target.x - unit.getPositionX();
        int dy = target.y - unit.getPositionY();
        double distance = Math.hypot(dx, dy);

        int speed = Math.max(2, hexRadius / 15);

        if (distance <= speed) {
            unit.getHex().getUnits().remove(unit);
            unit.getTargetHex().getUnits().add(unit);
            unit.setHex(unit.getTargetHex());
            unit.setTargetHex(null);
            unit.setMoving(false);

            Point finalPos = getHexCenter(unit.getHex());
            unit.setPositionX(finalPos.x);
            unit.setPositionY(finalPos.y);

            if (onUnitArrived != null) onUnitArrived.run();

        } else {
            unit.setPositionX(unit.getPositionX() + (int) Math.round(speed * dx / distance));
            unit.setPositionY(unit.getPositionY() + (int) Math.round(speed * dy / distance));
        }
    }

    public void zoom(int direction) {
        int newRadius = hexRadius + direction * ZOOM_STEP;
        if (newRadius < MIN_RADIUS || newRadius > MAX_RADIUS) return;

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        int newDx = (int) (SQRT3 * newRadius);
        int newDy = (int) (1.5 * newRadius);

        cameraX = (int) ((cameraX + cx) * (double) newDx / dx) - cx;
        cameraY = (int) ((cameraY + cy) * (double) newDy / dy) - cy;

        hexRadius = newRadius;
        rebuildHexShape();

        cameraX = Math.max(-getWidth() + dx,  Math.min(map.getCols() * dx - dx, cameraX));
        cameraY = Math.max(-getHeight() + dy, Math.min(map.getRows() * dy - dy, cameraY));

        for (Unit unit : gameState.getUnits()) {
            if (!unit.isMoving() && unit.getHex() != null) {
                Point p = getHexCenter(unit.getHex());
                unit.setPositionX(p.x);
                unit.setPositionY(p.y);
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawHexTiles(g);
        drawBuildings(g);
        drawHexHighlight(g);
        drawEmpireBorder(g);
        drawRawResources(g);
        drawUnits(g);
    }

    public void setMap(Map map) {
        this.map = map;
        repaint();
    }

    public void moveCamera(int deltaX, int deltaY) {
        int screenW = getWidth();
        int screenH = getHeight();

        int mapWorldW = map.getCols() * dx;
        int mapWorldH = map.getRows() * dy;

        cameraX = Math.max(-screenW + dx, Math.min(mapWorldW - dx, cameraX + deltaX));
        cameraY = Math.max(-screenH + dy, Math.min(mapWorldH - dy, cameraY + deltaY));
    }

    public Hex getHexAt(int screenX, int screenY) {
        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                int px = (int) ((c + 0.5 * (r & 1)) * dx) - cameraX;
                int py = (int) (r * dy) - cameraY;
                Polygon tile = translatePolygon(hexShape, px, py);
                if (tile.contains(screenX, screenY)) {
                    return map.getHex(c, r);
                }
            }
        }
        return null;
    }

    private void drawHexTiles(Graphics g) {
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
                    Shape oldClip = g2.getClip();
                    g2.setClip(tile);
                    g2.drawImage(img, center.x - hexRadius, center.y - hexRadius,
                            hexRadius * 2, hexRadius * 2, null);
                    g2.setClip(oldClip);
                } else {
                    g2.setColor(Color.BLACK);
                    g2.fillPolygon(tile);
                    g2.setColor(Color.WHITE);
                    g2.drawPolygon(tile);
                    if (gameState.getSelectedHex() != hex) {
                        g2.setFont(new Font("Pixel Game", Font.BOLD, Math.max(12, hexRadius / 2)));
                        g2.drawString("?", center.x - hexRadius / 10, center.y + hexRadius / 10);
                    }
                }
            }
        }
    }

    private Point getHexCenter(int c, int r) {
        int px = (int) ((c + 0.5 * (r & 1)) * dx) - cameraX;
        int py = (int) (r * dy) - cameraY;
        return new Point(px, py);
    }

    public Point getHexCenter(Hex hex) {
        return getHexCenter(hex.getC(), hex.getR());
    }

    private void drawBuildings(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Building building : gameState.getBuildings()) {
            BufferedImage img = getBuildingImage(building);
            if (img == null) continue;

            Point center = getHexCenter(building.getHex().getC(), building.getHex().getR());
            Polygon tile = translatePolygon(hexShape, center.x, center.y);

            Shape oldClip = g2.getClip();
            g2.setClip(tile);
            g2.drawImage(img, center.x - hexRadius, center.y - hexRadius,
                    hexRadius * 2, hexRadius * 2, null);
            g2.setClip(oldClip);
        }
    }

    private void drawHexHighlight(Graphics g) {
        Hex selected = gameState.getSelectedHex();
        if (selected != null) {
            Graphics2D g2 = (Graphics2D) g;
            Point center = getHexCenter(selected);
            Polygon tile = translatePolygon(hexShape, center.x, center.y);
            g2.setColor(new Color(238, 255, 0));
            g2.setStroke(new BasicStroke(4));
            g2.drawPolygon(tile);
            if(gameState.getSelectedUnit()!=null){
                for (Hex hex:map.getNeighbourHexes(selected)){
                    Point ncenter = getHexCenter(hex);
                    Polygon ntile = translatePolygon(hexShape, ncenter.x, ncenter.y);
                    g2.drawPolygon(ntile);
                }
            }
        }
    }

    private Polygon translatePolygon(Polygon p, int offsetX, int offsetY) {
        Polygon result = new Polygon();
        for (int i = 0; i < p.npoints; i++) {
            result.addPoint(p.xpoints[i] + offsetX, p.ypoints[i] + offsetY);
        }
        return result;
    }

    private BufferedImage getTerrainImage(Hex.TerrainType terrain) {
        return switch (terrain) {
            case PLAIN    -> plainImage;
            case FOREST   -> forestImage;
            case MOUNTAIN -> mountainImage;
            case MEADOW   -> meadowImage;
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

        int iconSize   = Math.max(10, hexRadius / 5);
        int badgeRadius = iconSize / 2 + 7;
        int spacing    = badgeRadius * 2 + 4;

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null || !hex.isDiscovered() || hex.getBuilding() != null) continue;

                HashMap<Hex.ResourceType, Integer> resources = hex.getResources();
                if (resources.isEmpty()) continue;

                Point center = getHexCenter(c, r);
                int rowWidth = spacing * resources.size();
                int startX  = center.x - rowWidth / 2 + spacing / 2;
                int y       = center.y + hexRadius / 2 + badgeRadius;

                for (int i = 0; i < resources.size(); i++) {
                    BufferedImage icon = resourceIcons.get(resources.keySet().toArray()[i]);
                    if (icon == null) continue;

                    int iconX  = startX + i * spacing;
                    Polygon badge = createMiniHex(iconX, y - 2 * iconSize, badgeRadius);

                    g2.setColor(new Color(0, 0, 0, 160));
                    g2.fillPolygon(badge);
                    g2.setColor(new Color(255, 255, 255, 90));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawPolygon(badge);

                    g2.drawImage(icon, iconX - iconSize / 2, y - 5 * iconSize / 2, iconSize, iconSize, null);
                }
            }
        }
    }

    private void drawUnits(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int unitSize = Math.max(20, hexRadius / 2);

        initUnitPositions();

        for (Unit unit : gameState.getUnits()) {
            if (unit.isMoving() || (!unit.isAssigned() && !(unit.getHex().getBuilding()instanceof TownHall))) {
                int drawX = unit.getPositionX();
                int drawY = unit.getPositionY();
                String iconPath = "/utils/"
                        + unit.getClass().getName()
                        .toLowerCase()
                        .replace("model.", "")
                        .replace("unit.", "")
                        + ".png";
                ImageIcon icon = ImageLoader.getInstance().loadIcon(iconPath, unitSize);
                if (icon != null) {
                    g2.drawImage(icon.getImage(), drawX - unitSize / 2, drawY - unitSize / 2, unitSize, unitSize, null);
                }
                int barWidth = unitSize/4*3;
                int barHeight = 4;
                int barX = drawX-barWidth/2;
                int barY = drawY+barWidth/4;
                double apf = (double)unit.getActionPoints()/unit.getActionPointsMax();
                g2.setColor(new Color(60,60,60));
                g2.fillRect(barX, barY, barWidth, barHeight);
                g2.setColor(new Color(50,200,50));
                g2.fillRect(barX, barY, (int)(apf*barWidth), barHeight);
            }
        }
    }

    private void drawEmpireBorder(Graphics g) {
        if (map == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 31, 232));
        g2.setStroke(new BasicStroke(Math.max(1f, hexRadius / 20f),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int r = 0; r < map.getRows(); r++) {
            for (int c = 0; c < map.getCols(); c++) {
                Hex hex = map.getHex(c, r);
                if (hex == null || !hex.isInEmpireBorder()) continue;

                Point center = getHexCenter(c, r);
                Polygon tile = translatePolygon(hexShape, center.x, center.y);

                java.util.List<Hex> neighbours = map.getNeighbourHexes(hex);
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

    private BufferedImage getBuildingImage(Building building) {
        if (building instanceof TownHall)   return townHallImage;
        if (building instanceof Farm)       return farmHexImage;
        if (building instanceof LumberMill) return lumberMillHexImage;
        if (building instanceof StoneMine)  return stoneMineHexImage;
        if (building instanceof IronMine)   return ironMineHexImage;
        if (building instanceof Stable)     return stableHexImage;
        if (building instanceof Town)       return townHexImage;
        return null;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setOnUnitArrived(Runnable onUnitArrived) {
        this.onUnitArrived = onUnitArrived;
    }
}