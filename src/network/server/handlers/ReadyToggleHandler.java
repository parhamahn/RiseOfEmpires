package network.server.handlers;

import model.state.Lobby;
import network.request.ReadyToggleRequest;
import network.server.MessageHandler;
import network.server.Messenger;

import java.util.UUID;

public class ReadyToggleHandler implements MessageHandler<ReadyToggleRequest> {

    private final Lobby lobby;
    private final Messenger messenger;

    public ReadyToggleHandler(Lobby lobby, Messenger messenger) {
        this.lobby = lobby;
        this.messenger = messenger;
    }

    @Override
    public void handle(UUID senderID, ReadyToggleRequest message) {
        lobby.getPlayers().get(senderID).setReady(message.isReady());
    }

}
