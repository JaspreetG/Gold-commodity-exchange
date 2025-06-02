package io.goldexchange.trade_service.model;


import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;                //it is not for unique trade

    private Long userId;  
    private Double price;
    private int quantity;
    private Timestamp createdAt;
    private String side; // BUY or SELL

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

}


