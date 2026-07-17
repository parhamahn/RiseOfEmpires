package view.game.map;
import controller.SelectionContext;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import view.game.map.disasters.earthquake.CrackEffectLayer;
import view.game.map.disasters.earthquake.ScreenShake;
import view.game.map.disasters.flood.FloodEffectLayer;
import view.game.map.disasters.tsunami.TsunamiEffectLayer;
import view.game.map.season.RainEffectLayer;
import view.game.map.season.SeasonLightingLayer;
import view.game.map.season.SnowEffectLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MapPanel extends JPanel {

    private Map map;
    private final GameState gameState;
    private final SelectionContext selectionContext;
    private final Camera camera;
    private final UnitAnimator unitAnimator;
    private final RainEffectLayer rainEffectLayer;
    private final SnowEffectLayer snowEffectLayer;
    private final SeasonLightingLayer seasonLightingLayer;
    private final ScreenShake screenShake = new ScreenShake();
    private final CrackEffectLayer crackEffectLayer = new CrackEffectLayer();
    private final FloodEffectLayer floodEffectLayer = new FloodEffectLayer();
    private final TsunamiEffectLayer tsunamiEffectLayer = new TsunamiEffectLayer();
    private final List<MapLayer> staticLayers = new ArrayList<>();
    private final List<MapLayer> dynamicLayers = new ArrayList<>();

    private BufferedImage staticBuffer;
    private boolean staticDirty = true;


    public MapPanel(Map map, GameState gameState, SelectionContext selectionContext) {
        this.gameState = gameState;
        this.selectionContext = selectionContext;
        this.camera = new Camera();
        this.unitAnimator = new UnitAnimator(gameState);
        this.rainEffectLayer = new RainEffectLayer();
        this.snowEffectLayer = new SnowEffectLayer();
        this.seasonLightingLayer = new SeasonLightingLayer();

        setBackground(Color.BLACK);
        setMap(map);

        staticLayers.add(new TerrainLayer());
        staticLayers.add(new BuildingLayer());
        staticLayers.add(new BorderLayer());
        staticLayers.add(new ResourceLayer());

        dynamicLayers.add(new HighlightLayer());
        dynamicLayers.add(new UnitLayer());
        dynamicLayers.add(rainEffectLayer);
        dynamicLayers.add(snowEffectLayer);
        dynamicLayers.add(crackEffectLayer);
        dynamicLayers.add(seasonLightingLayer);
        dynamicLayers.add(floodEffectLayer);
        dynamicLayers.add(tsunamiEffectLayer);

        staticLayers.add(new WallLayer());

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() > 0 && getHeight() > 0) {
                    camera.setViewport(getWidth(), getHeight());
                    centerCameraOnTownHall();
                    unitAnimator.initPositions(camera);
                    staticDirty = true;
                    removeComponentListener(this);
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (getWidth() > 0 && getHeight() > 0) {
                    camera.setViewport(getWidth(), getHeight());
                    staticDirty = true;
                }
            }
        });
    }


    public void setMap(Map map) {
        this.map = map;
        unitAnimator.initPositions(camera);
        centerCameraOnTownHall();
        staticDirty = true;
        repaint();
    }

    public void markStaticDirty() {
        staticDirty = true;
    }

    public boolean tick() {
        boolean unitMoving = unitAnimator.tick(camera);
        boolean seasonActive = rainEffectLayer.isActive(gameState) || snowEffectLayer.isActive(gameState);
        boolean isShakinng = screenShake.tick();
        boolean cracksActive = crackEffectLayer.tick();
        boolean floodActive = floodEffectLayer.tick();
        boolean tsunamiActive = tsunamiEffectLayer.tick();

        if (unitMoving || seasonActive || isShakinng || cracksActive || floodActive || tsunamiActive) {

            repaint();
            return true;
        }
        return false;
    }

    private void rebuildStaticBuffer() {
        if (getWidth() <= 0 || getHeight() <= 0) return;
        staticBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = staticBuffer.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (MapLayer layer : staticLayers) {
            layer.draw(g2, map, gameState, selectionContext, camera);
        }
        g2.dispose();
        staticDirty = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (staticDirty || staticBuffer == null) rebuildStaticBuffer();
        g.translate(screenShake.getOffsetX(), screenShake.getOffsetY());
        g.drawImage(staticBuffer, 0, 0, null);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (MapLayer layer : dynamicLayers) {
            layer.draw(g2, map, gameState, selectionContext, camera);
        }
    }

    public void zoom(int direction) {
        if (getWidth() == 0 || getHeight() == 0) return;
        int mapW = map.getCols() * camera.getDx();
        int mapH = map.getRows() * camera.getDy();
        if (camera.zoom(direction, mapW, mapH, getWidth(), getHeight())) {
            unitAnimator.syncPositions(camera);
            staticDirty = true;
            repaint();
        }
    }

    public void moveCamera(int deltaX, int deltaY) {
        if (getWidth() == 0 || getHeight() == 0) return;
        int mapW = map.getCols() * camera.getDx();
        int mapH = map.getRows() * camera.getDy();
        camera.moveCamera(deltaX, deltaY, mapW, mapH, getWidth(), getHeight());
        unitAnimator.syncPositions(camera);
        staticDirty = true;
        repaint();
    }

    public Hex getHexAt(int screenX, int screenY) {
        return camera.getHexAt(screenX, screenY, map);
    }

    public Point getHexCenter(Hex hex) {
        return camera.getHexCenter(hex);
    }

    public void centerCameraOnHex(Hex hex) {
        if (hex == null || getWidth() == 0 || getHeight() == 0) return;
        int worldX = (int) ((hex.getC() + 0.5 * (hex.getR() & 1)) * camera.getDx());
        int worldY = hex.getR() * camera.getDy();
        camera.centerOn(worldX, worldY, getWidth(), getHeight());
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

    public void startShake(){
        screenShake.start();
    }
    public void showCracks(List<Hex> hexes) {
        crackEffectLayer.addCracks(hexes);
    }
    public void showFlood(Hex center, List<Hex> hexes) {
        floodEffectLayer.addWater(center, hexes);
    }

    public void showTsunami(Hex center, List<Hex> hexes) {
        tsunamiEffectLayer.addWater(center, hexes);
    }
}
