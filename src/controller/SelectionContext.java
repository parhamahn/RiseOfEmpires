package controller;

import model.map.Hex;
import model.unit.Unit;

public class SelectionContext {
    private Hex selectedHex;
    private Unit selectedUnit;

    public Hex getSelectedHex() { return selectedHex; }
    public void setSelectedHex(Hex selectedHex) { this.selectedHex = selectedHex; }
    public Unit getSelectedUnit() { return selectedUnit; }
    public void setSelectedUnit(Unit selectedUnit) { this.selectedUnit = selectedUnit; }


    public void reset(){
        selectedHex = null;
        selectedUnit = null;
    }
}
