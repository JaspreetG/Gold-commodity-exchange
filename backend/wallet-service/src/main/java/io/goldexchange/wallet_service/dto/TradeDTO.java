package io.goldexchange.wallet_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object (DTO) encapsulating the details of a successfully executed trade.
 * Used internally across microservices to process the financial impact (fiat and gold transfers)
 * on the wallets of the users involved in the trade.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {

    /**
     * The unique identifier of the user acting as the buyer in the trade.
     * This user will pay fiat currency and receive gold.
     */
    private String buyUserId;  //buyer

    /**
     * The unique identifier of the user acting as the seller in the trade.
     * This user will surrender gold and receive fiat currency.
     */
    private String sellUserId; // seller

    /**
     * The identifier of the specific buy order that was matched in this trade.
     */
    private String buyOrderId;  //buyer

    /**
     * The identifier of the specific sell order that was matched in this trade.
     */
    private String sellOrderId;  // seller

    /**
     * The agreed-upon execution price per unit of gold for this trade.
     */
    private Double price;

    /**
     * The total quantity of gold exchanged in this specific trade execution.
     */
    private int quantity;

}



