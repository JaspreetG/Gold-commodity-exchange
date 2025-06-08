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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;               

    private Long userId;  
    private Double price;
    private int quantity;
    private String side; // BUY or SELL
    private String type; // MARKET or LIMIT
    private Timestamp createdAt;

    @PrePersist
    public void onPrePersist() {
        if (createdAt == null) {
            createdAt = new Timestamp(System.currentTimeMillis());
        }
    }

}


