package network.response;

import network.common.MessageType;
import network.common.Response;

import java.util.UUID;

public class ActionResultResponse extends Response {

    private boolean success;
    private String errorMessage;

    public ActionResultResponse() {
        super();
    }

    public ActionResultResponse(UUID senderID, boolean success, String errorMessage) {
        super(MessageType.ACTION_RESULT_RESPONSE, senderID, System.currentTimeMillis());
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
}
