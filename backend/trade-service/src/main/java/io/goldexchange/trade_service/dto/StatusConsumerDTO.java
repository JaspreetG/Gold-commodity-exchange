
package io.goldexchange.trade_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for consuming order status updates from Kafka.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusConsumerDTO {

    /**
     * The unique identifier of the order.
     */
    private String orderId;

    /**
     * The identifier of the user who placed the order.
     */
    private String userId;

    /**
     * The side of the order (buyer/seller).
     */
    private String side;

    /**
     * The remaining or affected quantity.
     */
    private int quantity; 
   
}

