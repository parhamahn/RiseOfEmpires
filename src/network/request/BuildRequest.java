package network.request;

import model.Task;
import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class BuildRequest extends Request {

    private Task.TASK taskType;
    private int col;
    private int row;

    public BuildRequest(UUID senderID, Task.TASK taskType, int col, int row) {
        super(MessageType.BUILD_REQUEST, senderID, System.currentTimeMillis());
        this.taskType = taskType;
        this.col = col;
        this.row = row;
    }


    public Task.TASK getTaskType() { return taskType; }
    public int getCol() { return col; }
    public int getRow() { return row; }
}
