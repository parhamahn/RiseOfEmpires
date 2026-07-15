package view.game.map.season;

import controller.SelectionContext;
import model.Season;
import model.map.Map;
import model.state.GameState;
import view.game.map.Camera;
import view.game.map.MapLayer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class WeatherEffectLayer implements MapLayer {

    protected final List<Particle> particles = new ArrayList<>();
    protected final Random rand = new Random();
    private boolean initialized = false;

    protected abstract Season.SeasonType getActiveSeason();
    protected abstract int getParticleCount();
    protected abstract Particle spawnParticle(int width, int height, Random rand, boolean initial);
    protected abstract void moveParticle(Particle p);
    protected abstract void drawParticle(Graphics2D g2, Particle p);

    public boolean isActive(GameState gameState) {
        return gameState.getEmpireState().getSeason() == getActiveSeason();
    }

    private void initParticles(int width, int height) {
        particles.clear();
        rand.setSeed(System.nanoTime());
        for (int i = 0; i < getParticleCount(); i++) {
            particles.add(spawnParticle(width, height, rand, true));
        }
        initialized = true;
    }

    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        if (!isActive(gameState)) {
            initialized = false;
            return;
        }

        Rectangle bounds = g2.getClipBounds();
        if (bounds == null || bounds.width == 0 || bounds.height == 0) return;

        if (!initialized) {
            initParticles(bounds.width, bounds.height);
        }

        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            moveParticle(p);
            if (p.y > bounds.height + 50 || p.x > bounds.width + 150 || p.x < -150) {
                particles.set(i, spawnParticle(bounds.width, bounds.height, rand, false));
            }
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Particle p : particles) {
            drawParticle(g2, p);
        }
    }
}