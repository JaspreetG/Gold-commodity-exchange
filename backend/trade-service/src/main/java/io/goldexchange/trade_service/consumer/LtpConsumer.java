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

@Component
public class LtpConsumer {

    private final LtpCache ltpCache;

    public LtpConsumer(LtpCache ltpCache) {
        this.ltpCache = ltpCache;
    }

    @KafkaListener(topics = "ltp", groupId = "MatchingEngine-group")
    public void listenLtp(String message) {
        ltpCache.set(message); // Just store
    }
}