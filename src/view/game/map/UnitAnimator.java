package view.game.map;

import model.state.GameState;
import model.unit.Unit;

import java.awt.*;

public class UnitAnimator {

    private final GameState gameState;
    private Runnable onUnitArrived;


    public UnitAnimator(GameState gameState) {
        this.gameState = gameState;
    }


    public void setOnUnitArrived(Runnable onUnitArrived) {
        this.onUnitArrived = onUnitArrived;
    }

    public void initPositions(Camera camera) {
        for (Unit unit : gameState.getEmpireState().getUnits()) {
            if (!unit.isMoving()) {
                Point p = camera.getHexCenter(unit.getHex());
                unit.setPositionX(p.x);
                unit.setPositionY(p.y);
            }
        }
    }

    public void syncPositions(Camera camera) {
        for (Unit unit : gameState.getEmpireState().getUnits()) {
            if (!unit.isMoving() && unit.getHex() != null) {
                Point p = camera.getHexCenter(unit.getHex());
                unit.setPositionX(p.x);
                unit.setPositionY(p.y);
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
        return anyMoving;
    }

    private void animateStep(Unit unit, Camera camera) {
        Point target = camera.getHexCenter(unit.getTargetHex());
        int dx = target.x - unit.getPositionX();
        int dy = target.y - unit.getPositionY();
        double distance = Math.hypot(dx, dy);

        int speed = Math.max(2, camera.getHexRadius() / 15);

        if (distance <= speed) {
            unit.getHex().getUnits().remove(unit);
            unit.getTargetHex().getUnits().add(unit);
            unit.setHex(unit.getTargetHex());
            unit.setTargetHex(null);
            unit.setMoving(false);

            Point finalPos = camera.getHexCenter(unit.getHex());
            unit.setPositionX(finalPos.x);
            unit.setPositionY(finalPos.y);

            if (onUnitArrived != null) onUnitArrived.run();
        } else {
            unit.setPositionX(unit.getPositionX() + (int) Math.round(speed * dx / distance));
            unit.setPositionY(unit.getPositionY() + (int) Math.round(speed * dy / distance));
        }
    }
}
