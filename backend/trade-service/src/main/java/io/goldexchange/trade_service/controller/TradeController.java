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

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/trade")
public class TradeController {
    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody OrderRequest orderRequest, Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401)
                        .body(java.util.Map.of("error", "Unauthorized: User not authenticated"));
            }
            Long userId = (Long) authentication.getPrincipal();

            System.out.println("checking wallet");
            boolean isPossible = tradeService.checkWallet(orderRequest);
            System.out.println("checking wallet done");

            if (isPossible == true) {

                tradeService.sendOrderToMatcher(orderRequest, userId);
                return ResponseEntity.ok(java.util.Map.of("status", "Order sent to MatchingEngine"));
            }
            return ResponseEntity.status(500)
                    .body(java.util.Map.of("error", "Failed to send order due to insufficient funds"));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(java.util.Map.of("error", "Failed to send order exception" + e.getMessage()));
        }
    }

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
                return ResponseEntity.ok(java.util.Map.of("message", "No past trades found"));
            }

            return ResponseEntity.ok(java.util.Map.of("status", "Trades sent sucessfully", "pastTrades", pastTrades));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to send trades"));
        }
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders(Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401)
                        .body(java.util.Map.of("error", "Unauthorized: User not authenticated"));
            }
            Long userId = (Long) authentication.getPrincipal();
            System.out.println("in get order Controller");

            List<OrderDTO> orders = tradeService.getOrders(userId);
            if (orders == null || orders.isEmpty()) {
                return ResponseEntity.ok(java.util.Map.of("message", "No orders found"));
            }

            return ResponseEntity.ok(java.util.Map.of("status", "Orders sent successfully", "orders", orders));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to send orders" + e.getMessage()));
        }
    }

}
