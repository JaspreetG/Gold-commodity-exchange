package io.goldexchange.controller;

import io.goldexchange.dto.TradeRequest;
import io.goldexchange.entity.GoldTrade;
import io.goldexchange.service.TradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {
    private final TradeService tradeService;

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/{userId}")
    public List<GoldTrade> getTrades(@PathVariable Long userId) {
        return tradeService.getTradesForUser(userId);
    }

    @PostMapping("/")
    public GoldTrade createTrade(@RequestBody TradeRequest request) {
        // perform transaction and return saved trade
        return null;
    }
}
