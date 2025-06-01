package io.goldexchange.trade_service.consumer;

import io.goldexchange.trade_service.model.Trade;
import io.goldexchange.trade_service.service.TradeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TradeConsumer {
    private final TradeService tradeService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TradeConsumer(TradeService tradeService, SimpMessagingTemplate messagingTemplate) {
        this.tradeService = tradeService;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "trade", groupId = "matcher-group")
    public void listenTrade(String message) {
        try {
            Trade trade = objectMapper.readValue(message, Trade.class);
            tradeService.saveTrade(trade);
            messagingTemplate.convertAndSend("/topic/trade", message);
        } catch (Exception e) {
            // Log error
        }
    }
}
