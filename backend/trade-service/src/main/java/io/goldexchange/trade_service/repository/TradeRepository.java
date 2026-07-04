package io.goldexchange.trade_service.repository;

import io.goldexchange.trade_service.model.Trade;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
/**
 * Repository interface for Trade entity operations.
 */
public interface TradeRepository extends JpaRepository<Trade, Long> {
    /**
     * Finds trades by user ID.
     *
     * @param userId The ID of the user.
     * @return A list of trades involving the user.
     */
    List<Trade> findByUserId(Long userId);

    /**
     * Finds a trade by order ID.
     *
     * @param orderId The ID of the order.
     * @return The trade associated with the order.
     */
    Trade findByOrderId(Long orderId);
}
