package io.goldexchange.trade_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.goldexchange.trade_service.dto.OrderProducerDTO;
import io.goldexchange.trade_service.dto.OrderRequest;
import io.goldexchange.trade_service.dto.TradeConsumerDTO;
import io.goldexchange.trade_service.model.Trade;
import io.goldexchange.trade_service.producer.OrderProducer;
import io.goldexchange.trade_service.repository.TradeRepository;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class TradeService {
    private final TradeRepository tradeRepository;
    private final OrderProducer orderProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TradeService(OrderProducer orderProducer, TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
        this.orderProducer = orderProducer;
    }

    public void sendOrderToMatcher(OrderRequest orderRequest,Long userId) throws Exception {

        OrderProducerDTO orderProducerDTO = new OrderProducerDTO();
        BeanUtils.copyProperties(orderRequest, orderProducerDTO);
        orderProducerDTO.setUserId(userId.toString());

        String orderJson = objectMapper.writeValueAsString(orderProducerDTO);
        orderProducer.sendOrder(orderJson);
    }

    public Trade saveTrade(TradeConsumerDTO tradeConsumerDTO) {
        Trade trade = new Trade();
        trade.setBuyerId(Long.parseLong(tradeConsumerDTO.getBuyerId()));
        trade.setSellerId(Long.parseLong(tradeConsumerDTO.getSellerId()));
        trade.setPrice(tradeConsumerDTO.getPrice());
        trade.setQuantity(tradeConsumerDTO.getQuantity());
        return tradeRepository.save(trade);
    }
}
