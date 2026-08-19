package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class TradeAcceptRequest extends Request {

    private UUID tradeOfferID;

    public TradeAcceptRequest() {
        super();
    }

    public TradeAcceptRequest(UUID senderID, UUID tradeOfferID) {
        super(MessageType.TRADE_ACCEPT_REQUEST, senderID, System.currentTimeMillis());
        this.tradeOfferID = tradeOfferID;
    }

    public UUID getTradeOfferID() {
        return tradeOfferID;
    }
}
