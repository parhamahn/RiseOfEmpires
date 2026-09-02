package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class MoveUnitRequest extends Request {
    private UUID unitId;
    private int toCol;
    private int toRow;

    public MoveUnitRequest() { super(); }

    public MoveUnitRequest(UUID senderID, UUID unitId, int toCol, int toRow) {
        super(MessageType.MOVE_UNIT_REQUEST, senderID, System.currentTimeMillis());
        this.unitId = unitId;
        this.toCol = toCol;
        this.toRow = toRow;
    }

    public UUID getUnitId() { return unitId; }
    public int getToCol() { return toCol; }
    public int getToRow() { return toRow; }
}
