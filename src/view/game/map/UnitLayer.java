package view.game.map;

import controller.SelectionContext;
import helpers.image.ImageLoader;
import model.map.Hex;
import model.map.Map;
import model.state.GameState;
import model.unit.Unit;
import view.common.UITheme;
import view.game.GameAssets;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UnitLayer implements MapLayer {

    private final UnitAnimator unitAnimator;

    public UnitLayer(UnitAnimator unitAnimator) {
        this.unitAnimator = unitAnimator;
    }

    @Override
    public void draw(Graphics2D g2, Map map, GameState gameState, SelectionContext selection, Camera camera) {
        int unitSize = Math.max(20, camera.getHexRadius() / 2);
        java.util.UUID playerId = gameState.getLocalPlayerId();

        List<Unit> units = new ArrayList<>();
        for(Unit unit:gameState.getWorldState().getEnemies())units.add(unit);
        for(Unit unit:gameState.getEmpireState().getUnits())units.add(unit);{}

        for (Unit unit : units) {
            if (unit.getHex() == null || !unit.getHex().isDiscoveredBy(playerId)) continue;
            if (unit.isMoving() || (!unit.isAssigned()
                    && (unit.getHex().getBuilding() == null || !unit.getHex().getBuilding().isTownHall()))) {
                if(unit.getHex().getTerrainType()== Hex.TerrainType.SEA && !unit.isMoving()){continue;}
                Point pos = unitAnimator.getPosition(unit);
                if (pos == null) continue;
                int drawX = pos.x;
                int drawY = pos.y;

                ImageIcon icon = ImageLoader.getInstance().loadIcon(
                        "/utils/" + GameAssets.unitImage(unit) + ".png", unitSize);
                if (icon != null) {
                    g2.drawImage(icon.getImage(), drawX - unitSize / 2, drawY - unitSize / 2,
                            unitSize, unitSize, null);
                }

                int barWidth = unitSize / 4 * 3;
                int barHeight = 4;
                int barX = drawX - barWidth / 2;
                int barY = drawY + barWidth / 4;
                double apf = (double) unit.getActionPoints() / unit.getActionPointsMax();
                double hpf = (double) unit.getHP()/unit.getMaxHP();
                UITheme.drawAPBar(g2, barX, barY, barWidth, barHeight, apf);
                UITheme.drawHPBar(g2, barX, barY+barHeight, barWidth, barHeight, hpf);
                g2.setColor(Color.RED);
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                g2.drawString(String.valueOf(unit.getHealth()), barX+barWidth, barY+barHeight*2);
            }
        }
    }
}
