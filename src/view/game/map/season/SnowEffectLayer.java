package view.game.map.season;

import model.Season;

import java.awt.*;
import java.util.Random;

public class SnowEffectLayer extends WeatherEffectLayer {

    private static final int FLAKE_COUNT = 250;
    private static final Color SNOW_COLOR = new Color(255, 255, 255, 220);
    private static final Color SNOW_COLOR_LIGHT = new Color(255, 255, 255, 140);
    private static final float SWAY_SPEED = 0.03f;
    private static final float SWAY_AMOUNT = 0.8f;

    @Override
    protected Season.SeasonType getActiveSeason() {
        return Season.SeasonType.WINTER;
    }

    @Override
    protected int getParticleCount() {
        return FLAKE_COUNT;
    }

    @Override
    protected Particle spawnParticle(int width, int height, Random rand, boolean initial) {
        float size = 2 + rand.nextFloat() * 4;
        float x = rand.nextFloat() * (width + 200) - 100;
        float y = initial ? rand.nextFloat() * (height + 100) : -size - rand.nextFloat() * 100;
        float speedY = 0.6f + rand.nextFloat() * 1.4f;
        float phase = rand.nextFloat() * (float) (Math.PI * 2);
        return new Particle(x, y, 0, speedY, size, phase);
    }

    @Override
    protected void moveParticle(Particle p) {
        p.y += p.speedY;
        p.phase += SWAY_SPEED;
        p.x += (float) Math.sin(p.phase) * SWAY_AMOUNT;
    }

    @Override
    protected void drawParticle(Graphics2D g2, Particle p) {
        g2.setColor(p.size > 4 ? SNOW_COLOR : SNOW_COLOR_LIGHT);
        g2.fillOval((int) p.x, (int) p.y, (int) p.size, (int) p.size);
    }
}