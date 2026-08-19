package network.notif;

import network.common.MessageType;
import network.common.Notif;

import java.util.UUID;

public class ChatMessageNotif extends Notif {

    private UUID senderPlayerID;
    private String senderName;
    private String content;
    private long sentAt;

    public ChatMessageNotif() {
        super();
    }

    public ChatMessageNotif(UUID serverID, UUID senderPlayerID, String senderName, String content, long sentAt) {
        super(MessageType.CHAT_MESSAGE_NOTIF, serverID, System.currentTimeMillis());
        this.senderPlayerID = senderPlayerID;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = sentAt;
    }

    public UUID getSenderPlayerID() { return senderPlayerID; }
    public String getSenderName() { return senderName; }
    public String getContent() { return content; }
    public long getSentAt() { return sentAt; }
}
