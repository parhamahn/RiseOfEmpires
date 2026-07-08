package view.game.map;

import controller.SelectionContext;
import model.map.Map;
import model.state.GameState;

import java.awt.*;

public interface MapLayer {
    void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera);
}
