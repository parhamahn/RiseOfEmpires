package controller;

import model.item.TeleportItem;
import model.map.Hex;
import model.unit.Unit;

public class SelectionContext {
    private Hex selectedHex;
    private Unit selectedUnit;
    private Hex wallOriginHex;
    private Hex roadOriginHex;
    private Hex destroyWallOriginHex;
    private Hex destroyRoadOriginHex;
    private Hex attackerHex;
    private TeleportItem teleportItemUsed;

    public Hex getSelectedHex() { return selectedHex; }
    public void setSelectedHex(Hex selectedHex) { this.selectedHex = selectedHex; }
    public Unit getSelectedUnit() { return selectedUnit; }
    public void setSelectedUnit(Unit selectedUnit) { this.selectedUnit = selectedUnit;
    }
    public Hex getWallOriginHex() { return wallOriginHex; }
    public void setWallOriginHex(Hex wallOriginHex) { this.wallOriginHex = wallOriginHex; }
    public Hex getRoadOriginHex() { return roadOriginHex; }
    public void setRoadOriginHex(Hex roadOriginHex) { this.roadOriginHex = roadOriginHex; }
    public Hex getDestroyWallOriginHex() { return destroyWallOriginHex; }
    public void setDestroyWallOriginHex(Hex destroyWallOriginHex) { this.destroyWallOriginHex = destroyWallOriginHex; }
    public Hex getDestroyRoadOriginHex() { return destroyRoadOriginHex; }
    public void setDestroyRoadOriginHex(Hex destroyRoadOriginHex) { this.destroyRoadOriginHex = destroyRoadOriginHex; }
    public Hex getAttackerHex() { return attackerHex; }
    public void setAttackerHex(Hex attacker) { this.attackerHex = attacker; }

    public TeleportItem getTeleportItemUsed() {
        return teleportItemUsed;
    }

    public void setTeleportItemUsed(TeleportItem teleportItemUsed) {
        this.teleportItemUsed = teleportItemUsed;
    }

    public void clearAllModes() {
        wallOriginHex = null;
        roadOriginHex = null;
        destroyWallOriginHex = null;
        destroyRoadOriginHex = null;
        attackerHex = null;
    }

    public void reset(){
        clearAllModes();
        selectedHex = null;
        selectedUnit = null;
    }
}