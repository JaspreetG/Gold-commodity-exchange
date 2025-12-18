package io.goldexchange.trade_service.dto;

import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for consuming trade execution details from Kafka.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeConsumerDTO {

    /**
     * The ID of the buyer.
     */
    private String buyUserId;

    /**
     * The ID of the seller.
     */
    private String sellUserId;

    /**
     * The ID of the buy order.
     */
    private String buyOrderId;

    /**
     * The ID of the sell order.
     */
    private String sellOrderId;

    /**
     * The execution price.
     */
    private Double price;

    /**
     * The executed quantity.
     */
    private int quantity;

}
