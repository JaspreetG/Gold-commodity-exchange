package io.goldexchange.trade_service.consumer;
import io.goldexchange.trade_service.dto.StatusConsumerDTO;
import io.goldexchange.trade_service.dto.TradeConsumerDTO;
import io.goldexchange.trade_service.service.TradeService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class StatusConsumer {

    private final TradeService tradeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StatusConsumer(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @KafkaListener(topics = "status", groupId = "matcher-group")
    public void listenTrade(String message) {
        try {
            StatusConsumerDTO statusConsumerDTO = objectMapper.readValue(message, StatusConsumerDTO.class);
            tradeService.updateOrder(statusConsumerDTO);
            System.out.println( " \\u001B[31m This is red text \\u001B[0m" );
            System.out.println(statusConsumerDTO);
            
            

        } catch (Exception e) {
            // Log error
            throw new RuntimeException("Failed to process trade message", e);
        }
    }
}

