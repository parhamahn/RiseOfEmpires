package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.Map;
import java.util.UUID;

public class TradeOfferRequest extends Request {

    private UUID targetPlayerID;
    private Map<String, Integer> giveResources;
    private Map<String, Integer> requestResources;

    public TradeOfferRequest() {
        super();
    }

    public TradeOfferRequest(UUID senderID, UUID targetPlayerID,
                             Map<String, Integer> giveResources, Map<String, Integer> requestResources) {
        super(MessageType.TRADE_OFFER_REQUEST, senderID, System.currentTimeMillis());
        this.targetPlayerID = targetPlayerID;
        this.giveResources = giveResources;
        this.requestResources = requestResources;
    }

    public UUID getTargetPlayerID() { return targetPlayerID; }
    public Map<String, Integer> getGiveResources() { return giveResources; }
    public Map<String, Integer> getRequestResources() { return requestResources; }
}
