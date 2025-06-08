package io.goldexchange.trade_service.dto;


import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long orderId;               
    private Long userId;  
    private Double price;
    private int quantity;
    private String side; // BUY or SELL
    private String type; // MARKET or LIMIT
    private Timestamp createdAt;

}


