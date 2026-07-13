package view.game.map.season;

import model.Season;

import java.awt.*;
import java.util.Random;

public class RainEffectLayer extends WeatherEffectLayer {

    private static final int DROP_COUNT = 400;
    private static final float ACCELERATION = 0.3f;
    private static final Color RAIN_COLOR = new Color(150, 180, 255, 100);
    private static final Color RAIN_COLOR_LIGHT = new Color(180, 200, 255, 60);
    private static final BasicStroke RAIN_STROKE = new BasicStroke(1.2f);

    @Override
    protected Season.SeasonType getActiveSeason() {
        return Season.SeasonType.FALL;
    }

    @Override
    protected int getParticleCount() {
        return DROP_COUNT;
    }

    @Override
    protected Particle spawnParticle(int width, int height, Random rand, boolean initial) {
        float length = 8 + rand.nextFloat() * 16;
        float x = rand.nextFloat() * (width + 200) - 100;
        float y = initial ? rand.nextFloat() * (height + 100) : -length - rand.nextFloat() * 100;
        float speedY = 4 + rand.nextFloat() * 6;
        return new Particle(x, y, 0, speedY, length, 0);
    }

    @Override
    protected void moveParticle(Particle p) {
        p.y += p.speedY;
        p.x += p.speedX;
        p.speedY += ACCELERATION;
    }

    @Override
    protected void drawParticle(Graphics2D g2, Particle p) {
        g2.setColor(p.size > 16 ? RAIN_COLOR : RAIN_COLOR_LIGHT);
        g2.setStroke(RAIN_STROKE);
        int endX = (int) p.x;
        int endY = (int) (p.y + p.size);
        g2.drawLine((int) p.x, (int) p.y, endX, endY);
    }
}