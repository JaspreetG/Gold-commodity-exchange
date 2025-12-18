package io.goldexchange.trade_service.consumer;

import io.goldexchange.trade_service.dto.StatusConsumerDTO;
import io.goldexchange.trade_service.service.TradeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka consumer for order status updates from the matching engine.
 */
@Component
public class StatusConsumer {

    private static final Logger logger = LoggerFactory.getLogger(StatusConsumer.class);

    private final TradeService tradeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StatusConsumer(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * Listens to the "status" topic and updates order statuses.
     *
     * @param message The JSON message received from Kafka.
     */
    @KafkaListener(topics = "status", groupId = "MatchingEngine-group")
    public void listenTrade(String message) {
        try {
            StatusConsumerDTO statusConsumerDTO = objectMapper.readValue(message, StatusConsumerDTO.class);
            tradeService.updateOrder(statusConsumerDTO);
            logger.info("Processed status update: {}", statusConsumerDTO);

        } catch (Exception e) {
            logger.error("Failed to process status message: {}", message, e);
            // Optionally decide if we should rethrow to trigger retry
            throw new RuntimeException("Failed to process trade message", e);
        }
    }
}
