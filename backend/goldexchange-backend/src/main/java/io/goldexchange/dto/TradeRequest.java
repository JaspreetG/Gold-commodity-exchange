package io.goldexchange.dto;

import lombok.Data;

@Data
public class TradeRequest {
    private Long buyerId;
    private Long sellerId;
    private double quantity;
    private double price;
}
