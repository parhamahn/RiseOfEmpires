package view.game.map;

import model.state.GameState;
import model.unit.Unit;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class UnitAnimator {

    private final GameState gameState;
    private final Map<Unit, Point> positions = new HashMap<>();
    private Runnable onUnitArrived;


    public UnitAnimator(GameState gameState) {
        this.gameState = gameState;
    }

    public Point getPosition(Unit unit) {
        return positions.get(unit);
    }

    public void setPosition(Unit unit, Point point) {
        positions.put(unit, point);
    }

    public void removePosition(Unit unit) {
        positions.remove(unit);
    }


    public void setOnUnitArrived(Runnable onUnitArrived) {
        this.onUnitArrived = onUnitArrived;
    }

    public void initPositions(Camera camera) {
        for (Unit unit : gameState.getEmpireState().getUnits()) {
            if (!unit.isMoving()) {
                Point p = camera.getHexCenter(unit.getHex());
                positions.put(unit, p);
            }
        }
        for (Unit unit : gameState.getWorldState().getEnemies()) {
            if (!unit.isMoving() && unit.getHex() != null) {
                Point p = camera.getHexCenter(unit.getHex());
                positions.put(unit, p);
            }
        }
    }

    public void syncPositions(Camera camera) {
        java.util.Set<Unit> allUnits = new java.util.HashSet<>(gameState.getEmpireState().getUnits());
        allUnits.addAll(gameState.getWorldState().getEnemies());
        positions.keySet().retainAll(allUnits);
        for (Unit unit : gameState.getEmpireState().getUnits()) {
            if (!unit.isMoving() && unit.getHex() != null) {
                Point p = camera.getHexCenter(unit.getHex());
                positions.put(unit, p);
            }
        }
        for (Unit unit : gameState.getWorldState().getEnemies()) {
            if (!unit.isMoving() && unit.getHex() != null) {
                Point p = camera.getHexCenter(unit.getHex());
                positions.put(unit, p);
            }
        }
    }

    public boolean tick(Camera camera) {
        boolean anyMoving = false;
        for (Unit unit : gameState.getEmpireState().getUnits()) {
            if (unit.isMoving()) {
                animateStep(unit, camera);
                anyMoving = true;
            }
        }
        for (Unit unit : gameState.getWorldState().getEnemies()) {
            if (unit.isMoving()) {
                animateStep(unit, camera);
                anyMoving = true;
            }
        }
        return anyMoving;
    }

    private void animateStep(Unit unit, Camera camera) {
        Point current = positions.getOrDefault(unit, new Point(0, 0));
        Point target = camera.getHexCenter(unit.getTargetHex());
        int dx = target.x - current.x;
        int dy = target.y - current.y;
        double distance = Math.hypot(dx, dy);

        int speed = Math.max(2, camera.getHexRadius() / 15);

        if (distance <= speed) {
            if (unit.getTargetHex().canAddUnitType(unit.getUnitType())) {
                unit.getHex().getUnits().remove(unit);
                unit.getTargetHex().getUnits().add(unit);
                unit.setHex(unit.getTargetHex());
            }
            unit.setTargetHex(null);
            unit.setMoving(false);

            Point finalPos = camera.getHexCenter(unit.getHex());
            positions.put(unit, finalPos);

            if (onUnitArrived != null) onUnitArrived.run();
        } else {
            positions.put(unit, new Point(
                    current.x + (int) Math.round(speed * dx / distance),
                    current.y + (int) Math.round(speed * dy / distance)));
        }
    }
}
