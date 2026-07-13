package view.game.map;

import controller.SelectionContext;
import model.Season;
import model.map.Map;
import model.state.GameState;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class SeasonEffectLayer implements MapLayer {

    private static final int DROP_COUNT = 400;
    private static final float ACCELERATION = 0.3f;
    private static final float WIND_ANGLE = 0;
    private static final Color RAIN_COLOR = new Color(150, 180, 255, 100);
    private static final Color RAIN_COLOR_LIGHT = new Color(180, 200, 255, 60);

    private final ArrayList<RainDrop> rainDrops = new ArrayList<>();
    private boolean initialized = false;

    private static class RainDrop {
        float x, y, speed, length;

        RainDrop(float x, float y, float speed, float length) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.length = length;
        }

        void move() {
            y += speed;
            x += speed * WIND_ANGLE;
            speed += ACCELERATION;
        }

        void reset(int panelWidth, Random rand) {
            x = rand.nextFloat() * (panelWidth + 200) - 100;
            y = -length - rand.nextFloat() * 100;
            speed = 4 + rand.nextFloat() * 6;
            length = 16 + rand.nextFloat() * 32;
        }
    }

    public boolean isActive(GameState gameState) {
        return gameState.getWorldState().getSeason() == Season.SeasonType.FALL;
    }

    private void initDrops(int width, int height) {
        rainDrops.clear();
        Random rand = new Random();
        for (int i = 0; i < DROP_COUNT; i++) {
            float x = rand.nextFloat() * (width + 200) - 100;
            float y = rand.nextFloat() * (height + 100);
            float speed = 4 + rand.nextFloat() * 6;
            float length = 8 + rand.nextFloat() * 16;
            rainDrops.add(new RainDrop(x, y, speed, length));
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
        if (bounds.width == 0 || bounds.height == 0) return;

        if (!initialized) {
            initDrops(bounds.width, bounds.height);
        }

        for (RainDrop drop : rainDrops) {
            drop.move();
            if (drop.y > bounds.height + 50 || drop.x > bounds.width + 100) {
                drop.reset(bounds.width, new Random());
            }
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (RainDrop drop : rainDrops) {
            g2.setColor(drop.length > 16 ? RAIN_COLOR : RAIN_COLOR_LIGHT);
            g2.setStroke(new BasicStroke(1.2f));
            int endX = (int) (drop.x + drop.length * WIND_ANGLE);
            int endY = (int) (drop.y + drop.length);
            g2.drawLine((int) drop.x, (int) drop.y, endX, endY);
        }
    }
}
