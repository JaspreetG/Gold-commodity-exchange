package io.goldexchange.trade_service.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String userId;
    private Double quantity;
    private Double price;
    private String side; // BUY or SELL
    private String type; // LIMIT or MARKET
}
