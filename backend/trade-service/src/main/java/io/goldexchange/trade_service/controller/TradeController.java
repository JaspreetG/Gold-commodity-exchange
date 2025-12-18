package io.goldexchange.trade_service.controller;

import io.goldexchange.trade_service.dto.OrderDTO;
import io.goldexchange.trade_service.dto.OrderRequest;
import io.goldexchange.trade_service.dto.PastTradeDTO;
import io.goldexchange.trade_service.service.TradeService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST Controller for Trade services.
 * Handles order creation, and retrieval of past trades and active orders.
 */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/trade")
public class TradeController {

    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    /**
     * Places a new order.
     *
     * @param orderRequest   The order details (price, quantity, side, type).
     * @param authentication The authentication object.
     * @return A response entity indicating success or failure.
     */
    @PostMapping("/createOrder")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderRequest orderRequest, Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401)
                        .body(java.util.Map.of("error", "Unauthorized: User not authenticated"));
            }
            Long userId = (Long) authentication.getPrincipal();

            logger.info("Checking wallet for user {}", userId);
            boolean isPossible = tradeService.checkWallet(orderRequest);

            if (isPossible) {
                tradeService.sendOrderToMatcher(orderRequest, userId);
                logger.info("Order sent to MatchingEngine for user {}", userId);
                return ResponseEntity.ok(java.util.Map.of("status", "Order sent to MatchingEngine"));
            }

            logger.warn("Insufficient funds for user {} order", userId);
            return ResponseEntity.status(500)
                    .body(java.util.Map.of("error", "Failed to send order due to insufficient funds"));
        } catch (Exception e) {
            logger.error("Exception while placing order for user {}: {}", authentication != null ? authentication.getPrincipal() : "unknown", e.getMessage(), e);
            return ResponseEntity.status(500)
                    .body(java.util.Map.of("error", "Failed to send order exception: " + e.getMessage()));
        }
    }

    /**
     * Retrieves past trades for the authenticated user.
     *
     * @param authentication The authentication object.
     * @return A list of past trades.
     */
    @GetMapping("/getPastTrades")
    public ResponseEntity<?> pastTrades(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401)
                        .body(java.util.Map.of("error", "Unauthorized: User not authenticated"));
            }
            Long userId = (Long) authentication.getPrincipal();

            List<PastTradeDTO> pastTrades = tradeService.pastTrades(userId);
            if (pastTrades == null || pastTrades.isEmpty()) {
                logger.debug("No past trades found for user {}", userId);
                return ResponseEntity.ok(java.util.Map.of("message", "No past trades found"));
            }

            logger.debug("Returned {} past trades for user {}", pastTrades.size(), userId);
            return ResponseEntity.ok(java.util.Map.of("status", "Trades sent sucessfully", "pastTrades", pastTrades));
        } catch (Exception e) {
            logger.error("Failed to fetch past trades for user {}: {}", authentication != null ? authentication.getPrincipal() : "unknown", e.getMessage(), e);
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to send trades"));
        }
    }

    /**
     * Retrieves active orders for the authenticated user.
     *
     * @param authentication The authentication object.
     * @return A list of active orders.
     */
    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401)
                        .body(java.util.Map.of("error", "Unauthorized: User not authenticated"));
            }
            Long userId = (Long) authentication.getPrincipal();

            List<OrderDTO> orders = tradeService.getOrders(userId);
            if (orders == null || orders.isEmpty()) {
                logger.debug("No active orders found for user {}", userId);
                return ResponseEntity.ok(java.util.Map.of("message", "No orders found"));
            }

            logger.debug("Returned {} active orders for user {}", orders.size(), userId);
            return ResponseEntity.ok(java.util.Map.of("status", "Orders sent successfully", "orders", orders));
        } catch (Exception e) {
            logger.error("Failed to fetch orders for user {}: {}", authentication != null ? authentication.getPrincipal() : "unknown", e.getMessage(), e);
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to send orders: " + e.getMessage()));
        }
    }

}
