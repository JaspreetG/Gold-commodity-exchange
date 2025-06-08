package io.goldexchange.trade_service.dto;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeConsumerDTO {

    private String buyOrderId; // buyer
    private String sellOrderId; // seller
    private Double price;
    private int quantity;

}
