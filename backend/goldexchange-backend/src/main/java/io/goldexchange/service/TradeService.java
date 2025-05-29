package io.goldexchange.service;

import io.goldexchange.entity.GoldTrade;
import io.goldexchange.repository.GoldTradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {
    private final GoldTradeRepository tradeRepository;

    public TradeService(GoldTradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }

    public List<GoldTrade> getTradesForUser(Long userId) {
        return tradeRepository.findByBuyerIdOrSellerIdOrderByTradedAtDesc(userId, userId);
    }
}
