package io.goldexchange.trade_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
// import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import io.goldexchange.trade_service.webSocket.LtpCache;

// @Component
// public class LtpConsumer {
//     private final SimpMessagingTemplate messagingTemplate;

//     public LtpConsumer(SimpMessagingTemplate messagingTemplate) {
//         this.messagingTemplate = messagingTemplate;
//     }

//     @KafkaListener(topics = "ltp", groupId = "MatchingEngine-group")
//     public void listenLtp(String message) {
//         // Forward LTP to WebSocket clients
//         messagingTemplate.convertAndSend("/topic/ltp", message);
//     }
// }

/**
 * Kafka Consumer for Last Traded Price (LTP) updates.
 */
@Component
public class LtpConsumer {

    private final LtpCache ltpCache;

    /**
     * Constructs the LtpConsumer.
     *
     * @param ltpCache The cache to store the latest LTP.
     */
    public LtpConsumer(LtpCache ltpCache) {
        this.ltpCache = ltpCache;
    }

    /**
     * Listens for LTP updates from Kafka.
     *
     * @param message The LTP message in JSON format.
     */
    @KafkaListener(topics = "ltp", groupId = "MatchingEngine-group")
    public void listenLtp(String message) {
        ltpCache.set(message); // Just store
    }
}