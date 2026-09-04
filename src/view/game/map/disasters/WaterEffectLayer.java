package view.game.map.disasters;

import controller.SelectionContext;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import view.game.map.Camera;
import view.game.map.MapLayer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class WaterEffectLayer implements MapLayer {

    private static final int SPLASH_TICKS = 24;

    protected enum Stage {WAITING, RISING, HOLD, RECEDING}

    protected class WaterHex {
        Hex hex;
        int delayTicks;
        int localTick;
        int splashTicks;
        double level;
        double wavePhase;
        Stage stage = Stage.WAITING;
    }

    private final List<WaterHex> activeHexes = new ArrayList<>();
    private final Random rand = new Random();

    protected abstract int riseTicks();
    protected abstract int holdTicks();
    protected abstract int recedeTicks();
    protected abstract int staggerPerRing();
    protected abstract Color deepColor();
    protected abstract Color shallowColor();
    protected abstract Color foamColor();
    protected abstract double waveAmplitude();
    protected abstract double waveSpeed();

    public void addWater(Hex center, List<Hex> hexes) {
        activeHexes.removeIf(w -> hexes.contains(w.hex));
        for (Hex hex : hexes) {
            if (hex == null) continue;
            WaterHex w = new WaterHex();
            w.hex = hex;
            w.delayTicks = hexDistance(center, hex) * staggerPerRing();
            w.wavePhase = rand.nextDouble() * Math.PI * 2;
            activeHexes.add(w);
        }
    }

    public boolean tick() {
        List<WaterHex> done = new ArrayList<>();
        for (WaterHex w : activeHexes) {
            if (w.delayTicks > 0) {
                w.delayTicks--;
                continue;
            }
            w.localTick++;
            w.wavePhase += waveSpeed();
            if (w.splashTicks > 0) w.splashTicks--;

            switch (w.stage) {
                case WAITING -> {
                    w.stage = Stage.RISING;
                    w.localTick = 0;
                    w.splashTicks = SPLASH_TICKS;
                }
                case RISING -> {
                    w.level = Math.min(1.0, w.localTick / (double) riseTicks());
                    if (w.localTick >= riseTicks()) {
                        w.stage = Stage.HOLD;
                        w.localTick = 0;
                    }
                }
                case HOLD -> {
                    w.level = 1.0;
                    if (w.localTick >= holdTicks()) {
                        w.stage = Stage.RECEDING;
                        w.localTick = 0;
                    }
                }
                case RECEDING -> {
                    w.level = Math.max(0.0, 1.0 - w.localTick / (double) recedeTicks());
                    if (w.localTick >= recedeTicks()) done.add(w);
                }
            }
        }
        activeHexes.removeAll(done);
        return !activeHexes.isEmpty();
    }

    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        if (activeHexes.isEmpty()) return;

        int r = camera.getHexRadius();
        Composite oldComposite = g2.getComposite();
        Shape oldClip = g2.getClip();
        Stroke oldStroke = g2.getStroke();

        for (WaterHex w : activeHexes) {
            if (w.delayTicks > 0) continue;
            Point center = camera.getHexCenter(w.hex);

            if (w.level > 0.001) {
                Polygon tile = camera.translatePolygon(camera.getHexShape(), center.x, center.y);
                g2.setClip(tile);

                int top = (int) (center.y + r - 2 * r * w.level);
                int waveOffset = (int) (Math.sin(w.wavePhase) * waveAmplitude());

                GradientPaint gradient = new GradientPaint(
                        center.x, top, shallowColor(),
                        center.x, center.y + r, deepColor());
                g2.setPaint(gradient);
                g2.fillRect(center.x - r, top + waveOffset, r * 2, r * 3);

                g2.setColor(foamColor());
                g2.setStroke(new BasicStroke(2f));
                Path2D wave = new Path2D.Double();
                wave.moveTo(center.x - r, top + waveOffset);
                for (int x = -r; x <= r; x += 4) {
                    double y = top + waveOffset + Math.sin(w.wavePhase + x * 0.15) * (waveAmplitude() * 0.4);
                    wave.lineTo(center.x + x, y);
                }
                g2.draw(wave);
                g2.setClip(oldClip);
            }

            if (w.splashTicks > 0) {
                float progress = 1f - (w.splashTicks / (float) SPLASH_TICKS);
                float radius = r * (0.3f + progress * 0.9f);
                float alpha = (1f - progress);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                g2.setColor(foamColor());
                g2.setStroke(new BasicStroke(3f));
                g2.draw(new Ellipse2D.Float(center.x - radius, center.y - radius, radius * 2, radius * 2));
                g2.setComposite(oldComposite);
            }
        }

        g2.setStroke(oldStroke);
        g2.setClip(oldClip);
        g2.setComposite(oldComposite);
    }

    private static int hexDistance(Hex a, Hex b) {
        int[] ac = offsetToCube(a.getC(), a.getR());
        int[] bc = offsetToCube(b.getC(), b.getR());
        return (Math.abs(ac[0] - bc[0]) + Math.abs(ac[1] - bc[1]) + Math.abs(ac[2] - bc[2])) / 2;
    }

    private static int[] offsetToCube(int col, int row) {
        int x = col - (row - (row & 1)) / 2;
        int z = row;
        int y = -x - z;
        return new int[]{x, y, z};
    }
}