package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class BuildRequest extends Request {

    private String taskType;
    private int col;
    private int row;

    public BuildRequest() {
        super();
    }

    public BuildRequest(UUID senderID, String taskType, int col, int row) {
        super(MessageType.BUILD_REQUEST, senderID, System.currentTimeMillis());
        this.taskType = taskType;
        this.col = col;
        this.row = row;
    }

    public String getTaskType() { return taskType; }
    public int getCol() { return col; }
    public int getRow() { return row; }
}
