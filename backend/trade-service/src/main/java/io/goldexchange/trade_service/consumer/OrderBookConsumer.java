package io.goldexchange.trade_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderBookConsumer {
    private final SimpMessagingTemplate messagingTemplate;

    public OrderBookConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "orderbook", groupId = "matcher-group")
    public void listenOrderBook(String message) {
        // Forward order book to WebSocket clients
        messagingTemplate.convertAndSend("/topic/orderbook", message);
    }
}
