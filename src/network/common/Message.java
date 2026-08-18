package network.common;

import java.util.UUID;

public abstract class Message {

    private UUID senderID;
    private long timeStamp;
    private MessageType messageType;


    protected Message(MessageType messageType, UUID senderID, long timeStamp){
        this.senderID = senderID;
        this.timeStamp = timeStamp;
        this.messageType = messageType;
    }


    public MessageType getMessageType(){
        return messageType;
    };

    public long getTimeStamp() {
        return timeStamp;
    }

    public UUID getSenderID() {
        return senderID;
    }

}
