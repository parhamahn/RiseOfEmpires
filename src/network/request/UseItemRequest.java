package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class UseItemRequest extends Request {

    private String itemType;
    private int targetUnitCol;
    private int targetUnitRow;
    private int targetHexCol;
    private int targetHexRow;

    public UseItemRequest() {
        super();
    }

    public UseItemRequest(UUID senderID, String itemType,
                          int targetUnitCol, int targetUnitRow,
                          int targetHexCol, int targetHexRow) {
        super(MessageType.USE_ITEM_REQUEST, senderID, System.currentTimeMillis());
        this.itemType = itemType;
        this.targetUnitCol = targetUnitCol;
        this.targetUnitRow = targetUnitRow;
        this.targetHexCol = targetHexCol;
        this.targetHexRow = targetHexRow;
    }

    public String getItemType() { return itemType; }
    public int getTargetUnitCol() { return targetUnitCol; }
    public int getTargetUnitRow() { return targetUnitRow; }
    public int getTargetHexCol() { return targetHexCol; }
    public int getTargetHexRow() { return targetHexRow; }
}
