package view.game.map.season;

public class Particle {
    float x, y;
    float speedX, speedY;
    float size;
    float phase;

    public Particle(float x, float y, float speedX, float speedY, float size, float phase) {
        this.x = x;
        this.y = y;
        this.speedX = speedX;
        this.speedY = speedY;
        this.size = size;
        this.phase = phase;
    }
}