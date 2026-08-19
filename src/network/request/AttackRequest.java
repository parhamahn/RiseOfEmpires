package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class AttackRequest extends Request {

    private int attackerCol;
    private int attackerRow;
    private int targetCol;
    private int targetRow;

    public AttackRequest() {
        super();
    }

    public AttackRequest(UUID senderID, int attackerCol, int attackerRow, int targetCol, int targetRow) {
        super(MessageType.ATTACK_REQUEST, senderID, System.currentTimeMillis());
        this.attackerCol = attackerCol;
        this.attackerRow = attackerRow;
        this.targetCol = targetCol;
        this.targetRow = targetRow;
    }

    public int getAttackerCol() { return attackerCol; }
    public int getAttackerRow() { return attackerRow; }
    public int getTargetCol() { return targetCol; }
    public int getTargetRow() { return targetRow; }
}
