package view.game.map;

import controller.SelectionContext;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.Unit;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HighlightLayer implements MapLayer {

    private static final Color HIGHLIGHT_COLOR = new Color(238, 255, 0);
    private static final float STROKE_WIDTH = 4;
    private static final BasicStroke HIGHLIGHT_STROKE = new BasicStroke(STROKE_WIDTH);


    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        Hex selected = selection.getSelectedHex();
        if (selected == null) return;

        Point center = camera.getHexCenter(selected);
        Polygon tile = camera.translatePolygon(camera.getHexShape(), center.x, center.y);

        g2.setColor(HIGHLIGHT_COLOR);
        g2.setStroke(HIGHLIGHT_STROKE);
        g2.drawPolygon(tile);

        if (selection.getSelectedUnit() != null || selection.getWallOriginHex() != null || selection.getRoadOriginHex() != null
                || selection.getDestroyWallOriginHex() != null || selection.getDestroyRoadOriginHex() != null) {
            for (Hex hex : map.getNeighbourHexes(selected)) {
                if (hex == null) continue;
                Point nCenter = camera.getHexCenter(hex);
                Polygon nTile = camera.translatePolygon(camera.getHexShape(), nCenter.x, nCenter.y);
                g2.drawPolygon(nTile);
            }
        }

        if(selection.getAttackerHex()!=null){
            List<Hex> hexes = new ArrayList<>();
            for (Hex hex : map.getNeighbourHexes(selected)) {
                if (hex == null) continue;
                if(hex.getUnits().stream().anyMatch(unit -> unit.getUnitType()== Unit.UNIT_TYPE.ARCHER)) {
                    for (Hex hex2 : map.getNeighbourHexes(hex)) {
                        if (hex2 == null) continue;
                        hexes.add(hex2);
                    }
                }
                if(!hexes.contains(hex)){hexes.add(hex);}
            }
            hexes.remove(selected);
            for(Hex hex : hexes){
                Point nCenter = camera.getHexCenter(hex);
                Polygon nTile = camera.translatePolygon(camera.getHexShape(), nCenter.x, nCenter.y);
                g2.drawPolygon(nTile);
            }
        }
    }
}
