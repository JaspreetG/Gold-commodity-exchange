package io.goldexchange.trade_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.goldexchange.trade_service.dto.OrderProducerDTO;
import io.goldexchange.trade_service.dto.OrderRequest;
import io.goldexchange.trade_service.dto.PastTradeDTO;
import io.goldexchange.trade_service.dto.TradeConsumerDTO;
import io.goldexchange.trade_service.model.Trade;
import io.goldexchange.trade_service.producer.OrderProducer;
import io.goldexchange.trade_service.repository.TradeRepository;

import java.util.List;

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

    public void sendOrderToMatcher(OrderRequest orderRequest, Long userId) throws Exception {

        OrderProducerDTO orderProducerDTO = new OrderProducerDTO();
        BeanUtils.copyProperties(orderRequest, orderProducerDTO);
        orderProducerDTO.setUserId(userId.toString());

        String orderJson = objectMapper.writeValueAsString(orderProducerDTO);
        orderProducer.sendOrder(orderJson);
    }

    public void saveTrade(TradeConsumerDTO tradeConsumerDTO) {

        try {
            // System.out.println("BeforBuySide");
            // System.out.println(tradeConsumerDTO.getBuyOrderId());
            // System.out.println(tradeConsumerDTO.getSellOrderId());
            // System.out.println(tradeConsumerDTO.getPrice());
            // System.out.println(tradeConsumerDTO.getQuantity());
            
            // buy side
            Trade tradeBuyerSide = new Trade();
            tradeBuyerSide.setUserId(Long.parseLong(tradeConsumerDTO.getBuyOrderId()));
            tradeBuyerSide.setPrice(tradeConsumerDTO.getPrice());
            tradeBuyerSide.setQuantity(tradeConsumerDTO.getQuantity());
            tradeBuyerSide.setSide("BUY");

            tradeRepository.save(tradeBuyerSide);

            // System.out.println("BeforSellSide");
            // System.out.println(tradeConsumerDTO.getBuyOrderId());
            // System.out.println(tradeConsumerDTO.getSellOrderId());
            // System.out.println(tradeConsumerDTO.getPrice());
            // System.out.println(tradeConsumerDTO.getQuantity());


            // seller side
            Trade tradeSellerSide = new Trade();
            tradeSellerSide.setUserId(Long.parseLong(tradeConsumerDTO.getSellOrderId()));
            tradeSellerSide.setPrice(tradeConsumerDTO.getPrice());
            tradeSellerSide.setQuantity(tradeConsumerDTO.getQuantity());
            tradeSellerSide.setSide("SELL");

            tradeRepository.save(tradeSellerSide);

        } catch (Exception e) {
            // TODO: handle exception
            throw new RuntimeException("Error saving trade: " + e.getMessage(), e);
        }
    }

    public List<PastTradeDTO> pastTrades(Long userId) {
        // Fetch trades where user is involved
        List<Trade> userTrades = tradeRepository.findByUserId(userId);

        if (userTrades == null || userTrades.isEmpty()) {
           return null;
        }

        List<PastTradeDTO> pastTrades= userTrades.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(trade -> {
                    PastTradeDTO pastTradeDTO = new PastTradeDTO();
                    pastTradeDTO.setUserId(trade.getUserId());
                    pastTradeDTO.setPrice(trade.getPrice());
                    pastTradeDTO.setQuantity(trade.getQuantity());
                    pastTradeDTO.setSide(trade.getSide());
                    pastTradeDTO.setCreatedAt(trade.getCreatedAt());
                    return pastTradeDTO;
                })
                .toList();

        return pastTrades;
    }


}
