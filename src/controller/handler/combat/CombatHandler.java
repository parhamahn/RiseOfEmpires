package controller.handler.combat;

import controller.SelectionContext;
import model.map.Hex;
import model.state.GameState;
import model.unit.Unit;
import view.combat.CombatMainPanel;
import view.game.GamePanel;

public class CombatHandler {

    private GameState gameState;
    private GamePanel gamePanel;
    private SelectionContext selectionContext;
    private CombatMain combatMain;


    public CombatHandler(GameState gameState, GamePanel gamePanel,  SelectionContext selectionContext) {
        this.gameState = gameState;
        this.gamePanel = gamePanel;
        this.selectionContext = selectionContext;
        this.combatMain = new CombatMain(gameState, gamePanel, selectionContext);
    }


    public void attachHandlers(){
        attachAttackButton();
        combatMain.attachHandlers();
    }

    public void handleAttackTarget(Hex attackerHex, Hex defenderHex){

        boolean anyArcher = selectionContext.getAttackerHex().getUnits().stream().anyMatch(unit -> unit.getUnitType()== Unit.UNIT_TYPE.ARCHER);
        int distance = gameState.getWorldState().getMap().getDistance(attackerHex, defenderHex);

        if(distance == 0 || (distance==2 && !anyArcher) || distance==-1){
            selectionContext.setAttackerHex(null);
            return;
        }

        gamePanel.getCombatMainPanel().setHexes(attackerHex, defenderHex);
        gamePanel.getCombatMainPanel().open();
        gamePanel.setAnyPanelOpen(true);

    }


    public GameState getGameState() {
        return gameState;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public void attachAttackButton(){
        gamePanel.getUnitListPanel().getAttackButton().addActionListener(e->{

            selectionContext.setAttackerHex(selectionContext.getSelectedHex());
            selectionContext.setDestroyWallOriginHex(null);
            selectionContext.setDestroyRoadOriginHex(null);
            gamePanel.repaint();

        });
    }


}
