package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class MoveUnitRequest extends Request {

    private int fromCol;
    private int fromRow;
    private int toCol;
    private int toRow;

    public MoveUnitRequest() {
        super();
    }

    public MoveUnitRequest(UUID senderID, int fromCol, int fromRow, int toCol, int toRow) {
        super(MessageType.MOVE_UNIT_REQUEST, senderID, System.currentTimeMillis());
        this.fromCol = fromCol;
        this.fromRow = fromRow;
        this.toCol = toCol;
        this.toRow = toRow;
    }

    public int getFromCol() { return fromCol; }
    public int getFromRow() { return fromRow; }
    public int getToCol() { return toCol; }
    public int getToRow() { return toRow; }
}
