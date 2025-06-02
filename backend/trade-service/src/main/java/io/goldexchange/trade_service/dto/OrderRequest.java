package io.goldexchange.trade_service.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long userId;
    private Double quantity;
    private Double price;
    private String side; // BUY or SELL
    private String type; // LIMIT or MARKET
}