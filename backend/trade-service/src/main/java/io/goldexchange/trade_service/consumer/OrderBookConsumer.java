package io.goldexchange.trade_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import io.goldexchange.trade_service.webSocket.OrderBookCache;

// @Component
// public class OrderBookConsumer {
//     private final SimpMessagingTemplate messagingTemplate;

//     public OrderBookConsumer(SimpMessagingTemplate messagingTemplate) {
//         this.messagingTemplate = messagingTemplate;
//     }

//     @KafkaListener(topics = "orderbook", groupId = "MatchingEngine-group")
//     public void listenOrderBook(String message) {
//         // Forward order book to WebSocket clients
//         messagingTemplate.convertAndSend("/topic/orderbook", message);
//     }
// }

@Component
public class OrderBookConsumer {
    private final OrderBookCache orderBookCache;

    public OrderBookConsumer(OrderBookCache orderBookCache) {
        this.orderBookCache = orderBookCache;
    }

    @KafkaListener(topics = "orderbook", groupId = "MatchingEngine-group")
    public void listenOrderBook(String message) {
        orderBookCache.set(message);
    }
}