package view.game.map.season;

import controller.SelectionContext;
import model.map.Map;
import model.state.GameState;
import view.game.map.Camera;
import view.game.map.MapLayer;

import java.awt.*;

public class SeasonLightingLayer implements MapLayer {

    private static final Color FALL_TINT = new Color(40, 30, 15, 45);
    private static final Color WINTER_TINT = new Color(15, 25, 55, 85);

    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        Color tint = switch (gameState.getWorldState().getSeason()) {
            case FALL -> FALL_TINT;
            case WINTER -> WINTER_TINT;
            default -> null;
        };
        if (tint == null) return;

        Rectangle bounds = g2.getClipBounds();
        if (bounds == null) return;

        g2.setColor(tint);
        g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}