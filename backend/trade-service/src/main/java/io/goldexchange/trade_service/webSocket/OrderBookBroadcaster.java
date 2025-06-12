package io.goldexchange.trade_service.webSocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderBookBroadcaster {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private OrderBookCache orderBookCache;

    @Scheduled(fixedRate = 3000)
    public void broadcastOrderBook() {
        String orderBook = orderBookCache.get();
        if (orderBook != null) {
            messagingTemplate.convertAndSend("/topic/orderbook", orderBook);
        }
    }
}