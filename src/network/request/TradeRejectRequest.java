package network.request;

import network.common.MessageType;
import network.common.Request;

import java.util.UUID;

public class TradeRejectRequest extends Request {

    private UUID tradeOfferID;

    public TradeRejectRequest() {
        super();
    }

    public TradeRejectRequest(UUID senderID, UUID tradeOfferID) {
        super(MessageType.TRADE_REJECT_REQUEST, senderID, System.currentTimeMillis());
        this.tradeOfferID = tradeOfferID;
    }

    public UUID getTradeOfferID() {
        return tradeOfferID;
    }
}
