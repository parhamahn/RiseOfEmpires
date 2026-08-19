package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class ChatMessageRequest extends Request {

    private String content;

    public ChatMessageRequest() {
        super();
    }

    public ChatMessageRequest(UUID senderID, String content) {
        super(MessageType.CHAT_MESSAGE_REQUEST, senderID, System.currentTimeMillis());
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
