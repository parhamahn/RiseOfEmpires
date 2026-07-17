package view.game.map.disasters.earthquake;

import java.util.Random;

public class ScreenShake {

    private static final int DURATION = 50;
    private static final int MAGNITUDE = 12;

    private final Random random = new Random();
    private int ticksRemaining = 0;
    private int offsetX = 0;
    private int offsetY = 0;

    public void start() {
        ticksRemaining = DURATION;
    }

    public boolean tick() {
        boolean shaking = ticksRemaining > 0;
        if (shaking) {
            ticksRemaining--;
            offsetX = random.nextInt(MAGNITUDE * 2 + 1) - MAGNITUDE;
            offsetY = random.nextInt(MAGNITUDE * 2 + 1) - MAGNITUDE;
        } else {
            offsetX = 0;
            offsetY = 0;
        }
        return shaking;
    }

    public int getOffsetX() { return offsetX; }
    public int getOffsetY() { return offsetY; }
}