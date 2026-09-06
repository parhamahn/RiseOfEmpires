package network.notif;

import network.common.MessageType;
import network.common.Notif;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ChatMessageNotif extends Notif {

    private UUID senderPlayerID;
    private String senderName;
    private String content;
    private String sentAt;

    public ChatMessageNotif() {
        super();
    }

    public ChatMessageNotif(UUID serverID, UUID senderPlayerID, String senderName, String content) {
        super(MessageType.CHAT_MESSAGE_NOTIF, serverID, System.currentTimeMillis());
        this.senderPlayerID = senderPlayerID;
        this.senderName = senderName;
        this.content = content;
        this.sentAt = new SimpleDateFormat("HH:mm").format(new Date());
    }

    public UUID getSenderPlayerID() { return senderPlayerID; }
    public String getSenderName() { return senderName; }
    public String getContent() { return content; }
    public String getSentAt() { return sentAt; }
}
