package view.game.map.disasters.earthquake;

import controller.SelectionContext;
import helpers.ImageLoader;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import view.game.map.Camera;
import view.game.map.MapLayer;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CrackEffectLayer implements MapLayer {

    private static final int FADE_IN_TICKS = 5;
    private static final int HOLD_TICKS = 60;
    private static final int FADE_OUT_TICKS = 30;
    private static final int TOTAL_TICKS = FADE_IN_TICKS + HOLD_TICKS + FADE_OUT_TICKS;

    private static final BufferedImage[] CRACK_IMAGES = {
            ImageLoader.getInstance().loadImage("/utils/crack1.png"),
            ImageLoader.getInstance().loadImage("/utils/crack2.png"),
            ImageLoader.getInstance().loadImage("/utils/crack3.png")
    };

    private final List<HexCrack> activeCracks = new ArrayList<>();
    private final Random rand = new Random();

    private static class HexCrack {
        Hex hex;
        BufferedImage image;
        double rotation;
        double scale;
        int ticksRemaining;
    }

    public void addCracks(List<Hex> hexes) {
        for (Hex hex : hexes) {
            if (hex == null || !hex.isDiscovered()) continue;
            HexCrack crack = new HexCrack();
            crack.hex = hex;
            crack.image = CRACK_IMAGES[rand.nextInt(CRACK_IMAGES.length)];
            crack.rotation = rand.nextDouble() * Math.PI * 2;
            crack.scale = 0.6 + rand.nextDouble() * 0.5;
            crack.ticksRemaining = TOTAL_TICKS;
            activeCracks.add(crack);
        }
    }

    public boolean tick() {
        activeCracks.removeIf(c -> --c.ticksRemaining <= 0);
        return !activeCracks.isEmpty();
    }

    private float alphaFor(HexCrack c) {
        int elapsed = TOTAL_TICKS - c.ticksRemaining;
        if (elapsed < FADE_IN_TICKS) {
            return elapsed / (float) FADE_IN_TICKS;
        } else if (c.ticksRemaining < FADE_OUT_TICKS) {
            return c.ticksRemaining / (float) FADE_OUT_TICKS;
        }
        return 1f;
    }

    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        if (activeCracks.isEmpty()) return;

        int r2 = camera.getHexRadius();
        Composite oldComposite = g2.getComposite();

        for (HexCrack c : activeCracks) {
            if (c.image == null) continue;
            Point center = camera.getHexCenter(c.hex);
            int size = (int) (r2 * 2 * c.scale);

            AffineTransform old = g2.getTransform();
            g2.translate(center.x, center.y);
            g2.rotate(c.rotation);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaFor(c)));
            g2.drawImage(c.image, -size / 2, -size / 2, size, size, null);
            g2.setTransform(old);
        }

        g2.setComposite(oldComposite);
    }
}