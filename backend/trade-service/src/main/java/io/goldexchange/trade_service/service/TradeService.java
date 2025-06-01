package io.goldexchange.trade_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.goldexchange.trade_service.dto.OrderRequest;
import io.goldexchange.trade_service.model.Trade;
import io.goldexchange.trade_service.producer.OrderProducer;
import io.goldexchange.trade_service.repository.TradeRepository;
import org.springframework.stereotype.Service;

@Service
public class TradeService {
    private final OrderProducer orderProducer;
    private final TradeRepository tradeRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TradeService(OrderProducer orderProducer, TradeRepository tradeRepository) {
        this.orderProducer = orderProducer;
        this.tradeRepository = tradeRepository;
    }

    public void sendOrderToMatcher(OrderRequest orderRequest) throws Exception {
        String orderJson = objectMapper.writeValueAsString(orderRequest);
        orderProducer.sendOrder(orderJson);
    }

    public Trade saveTrade(Trade trade) {
        return tradeRepository.save(trade);
    }
}
