package io.goldexchange.trade_service.controller;

import io.goldexchange.trade_service.dto.OrderRequest;
import io.goldexchange.trade_service.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("api/trade")
public class TradeController {
    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest,Authentication authentication) {
        try {
            if (authentication == null || authentication.getPrincipal() == null) {
                return ResponseEntity.status(401).body(java.util.Map.of("error", "Unauthorized: User not authenticated"));
            }
            Long userId = (Long) authentication.getPrincipal();
            orderRequest.setUserId(userId);
            
            tradeService.sendOrderToMatcher(orderRequest);
            return ResponseEntity.ok(java.util.Map.of("status", "Order sent to matcher"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of("error", "Failed to send order"));
        }
    }
}
