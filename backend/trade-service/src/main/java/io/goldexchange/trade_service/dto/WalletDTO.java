package io.goldexchange.trade_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Data Transfer Object for Wallet details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {

    /**
     * The unique identifier of the wallet.
     */
    private Long walletId;

    /**
     * The identifier of the user owning the wallet.
     */
    private Long userId;

    /**
     * The currency balance (e.g., INR).
     */
    private Double balance;

    /**
     * The gold balance (in grams).
     */
    private Double gold;
}

