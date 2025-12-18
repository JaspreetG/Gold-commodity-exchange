package io.goldexchange.trade_service.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Broadcaster for Order Book updates.
 * Periodically sends the latest order book from the cache to WebSocket clients.
 */
@Component
public class OrderBookBroadcaster {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private OrderBookCache orderBookCache;

    /**
     * Broadcasts the latest order book to "/topic/orderbook" every 3 seconds.
     */
    @Scheduled(fixedRate = 3000)
    public void broadcastOrderBook() {
        String orderBook = orderBookCache.get();
        if (orderBook != null) {
            messagingTemplate.convertAndSend("/topic/orderbook", orderBook);
        }
    }
}