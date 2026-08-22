package network.notif;

import network.common.MessageType;
import network.common.Notif;
import network.dto.GameStateDTO;

import java.util.UUID;

public class GameStateNotif extends Notif {

    private GameStateDTO state;

    public GameStateNotif(){
        super();
    }

    public GameStateNotif(UUID senderID, GameStateDTO state){
        super(MessageType.GAME_STATE_NOTIF, senderID, System.currentTimeMillis());
        this.state = state;
    }

    public GameStateDTO getState() { return state; }
}
