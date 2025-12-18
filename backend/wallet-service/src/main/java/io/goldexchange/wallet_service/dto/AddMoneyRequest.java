package io.goldexchange.wallet_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;

/**
 * DTO for adding money to a wallet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddMoneyRequest {

    /**
     * The amount of money to add. Must be positive.
     */
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;
}
