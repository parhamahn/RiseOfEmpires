package view.game.map;
import controller.SelectionContext;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class MapPanel extends JPanel {

    private Map map;
    private final GameState gameState;
    private final SelectionContext selectionContext;
    private final Camera camera;
    private final UnitAnimator unitAnimator;
    private final List<MapLayer> layers = new ArrayList<>();;


    public MapPanel(Map map, GameState gameState, SelectionContext selectionContext) {
        this.gameState = gameState;
        this.selectionContext = selectionContext;
        this.camera = new Camera();
        this.unitAnimator = new UnitAnimator(gameState);

        setBackground(Color.BLACK);
        setMap(map);

        layers.add(new TerrainLayer());
        layers.add(new BuildingLayer());
        layers.add(new HighlightLayer());
        layers.add(new BorderLayer());
        layers.add(new ResourceLayer());
        layers.add(new UnitLayer());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() > 0 && getHeight() > 0) {
                    camera.setViewport(getWidth(), getHeight());
                    centerCameraOnTownHall();
                    unitAnimator.initPositions(camera);
                    removeComponentListener(this);
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() > 0 && getHeight() > 0) {
                    camera.setViewport(getWidth(), getHeight());
                }
            }
        });
    }


    public void setMap(Map map) {
        this.map = map;
        unitAnimator.initPositions(camera);
        centerCameraOnTownHall();
        repaint();
    }

    public boolean tick() {
        if (unitAnimator.tick(camera)) {
            repaint();
            return true;
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (MapLayer layer : layers) {
            layer.draw(g2, map, gameState, selectionContext, camera);
        }
    }

    public void zoom(int direction) {
        if (getWidth() == 0 || getHeight() == 0) return;
        int mapW = map.getCols() * camera.getDx();
        int mapH = map.getRows() * camera.getDy();
        if (camera.zoom(direction, mapW, mapH, getWidth(), getHeight())) {
            unitAnimator.syncPositions(camera);
            repaint();
        }
    }

    public void moveCamera(int deltaX, int deltaY) {
        if (getWidth() == 0 || getHeight() == 0) return;
        int mapW = map.getCols() * camera.getDx();
        int mapH = map.getRows() * camera.getDy();
        camera.moveCamera(deltaX, deltaY, mapW, mapH, getWidth(), getHeight());
        unitAnimator.syncPositions(camera);
        repaint();
    }

    public Hex getHexAt(int screenX, int screenY) {
        return camera.getHexAt(screenX, screenY, map);
    }

    public Point getHexCenter(Hex hex) {
        return camera.getHexCenter(hex);
    }

    public void centerCameraOnTownHall() {
        Hex townHall = gameState.getEmpireState().getTownHallHex();
        if (townHall == null || getWidth() == 0 || getHeight() == 0) return;
        int worldX = (int) ((townHall.getC() + 0.5 * (townHall.getR() & 1)) * camera.getDx());
        int worldY = townHall.getR() * camera.getDy();
        camera.centerOn(worldX, worldY, getWidth(), getHeight());
    }

    public void setOnUnitArrived(Runnable onUnitArrived) {
        unitAnimator.setOnUnitArrived(onUnitArrived);
    }

    public GameState getGameState() {
        return gameState;
    }
}
