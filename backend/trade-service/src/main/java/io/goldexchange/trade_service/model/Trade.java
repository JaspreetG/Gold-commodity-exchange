package io.goldexchange.trade_service.model;

import java.security.Timestamp;

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
    private Long tradeId;

    private Long buyerId;  //buyer
    private Long sellerId;  // seller
    private Double price;
    private Double quantity;
    private Timestamp createdAt;

}


