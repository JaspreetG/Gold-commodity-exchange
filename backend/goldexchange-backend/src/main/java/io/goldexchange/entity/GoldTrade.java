package io.goldexchange.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Entity
@Data
public class GoldTrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trade_id;
    private Long buyer_id;
    private Long seller_id;
    private double quantity;
    private double price;
    private Instant traded_at;
}
