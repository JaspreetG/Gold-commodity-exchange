package io.goldexchange.repository;

import io.goldexchange.entity.GoldTrade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoldTradeRepository extends JpaRepository<GoldTrade, Long> {
    List<GoldTrade> findByBuyerIdOrSellerIdOrderByTradedAtDesc(Long buyerId, Long sellerId);
}
