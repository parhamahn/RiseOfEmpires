package network.notif;

import network.common.MessageType;
import network.common.Notif;

import java.util.UUID;

public class WarDeclaredNotif extends Notif {

    private UUID attackerPlayerID;
    private UUID defenderPlayerID;
    private String attackerName;
    private String defenderName;

    public WarDeclaredNotif() {
        super();
    }

    public WarDeclaredNotif(UUID senderID, UUID attackerPlayerID, UUID defenderPlayerID,
                            String attackerName, String defenderName) {
        super(MessageType.WAR_DECLARED_NOTIF, senderID, System.currentTimeMillis());
        this.attackerPlayerID = attackerPlayerID;
        this.defenderPlayerID = defenderPlayerID;
        this.attackerName = attackerName;
        this.defenderName = defenderName;
    }

    public UUID getAttackerPlayerID() { return attackerPlayerID; }
    public UUID getDefenderPlayerID() { return defenderPlayerID; }
    public String getAttackerName() { return attackerName; }
    public String getDefenderName() { return defenderName; }
}
