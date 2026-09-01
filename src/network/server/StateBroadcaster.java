package network.server;

import model.state.GameState;
import network.dto.GameStateDTO;
import network.dto.mapper.GameStateMapper;
import network.notif.GameStateNotif;

import java.util.List;
import java.util.UUID;

public class StateBroadcaster {

    public static void broadcast(GameState gameState, List<UUID> playerIds, Messenger messenger) {
        UUID previousContext = gameState.getActiveContextId();
        try {
            for (UUID playerId : playerIds) {
                gameState.setActiveContextId(playerId);
                GameStateDTO dto = GameStateMapper.INSTANCE.toDTO(gameState);
                messenger.sendTo(playerId, new GameStateNotif(null, dto));
            }
        } finally {
            gameState.setActiveContextId(previousContext);
        }
    }
}