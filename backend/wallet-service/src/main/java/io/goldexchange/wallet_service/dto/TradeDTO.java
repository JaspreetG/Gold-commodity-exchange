package io.goldexchange.wallet_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO representing trade details for wallet updates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {

    /**
     * The ID of the buyer.
     */
    private String buyUserId;  //buyer

    /**
     * The ID of the seller.
     */
    private String sellUserId; // seller

    /**
     * The ID of the buy order.
     */
    private String buyOrderId;  //buyer

    /**
     * The ID of the sell order.
     */
    private String sellOrderId;  // seller

    /**
     * The execution price.
     */
    private Double price;

    /**
     * The executed quantity.
     */
    private int quantity;

}



