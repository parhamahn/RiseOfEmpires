package network.notif;

import network.common.MessageType;
import network.common.Notif;

import java.util.UUID;

public class GameStateNotif extends Notif {

    public GameStateNotif(){
        super();
    }

    public GameStateNotif(UUID senderID){
        super(MessageType.GAME_STATE_NOTIF, senderID, System.currentTimeMillis());
    }
}
