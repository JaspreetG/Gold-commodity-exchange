package io.goldexchange.trade_service.dto;


import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PastTradeDTO {

    private Long userId;  
    private Long orderId;
    private Double price;
    private int quantity;
    private Timestamp createdAt;
    private String side; // BUY or SELL
    // private String type;

}


