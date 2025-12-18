package io.goldexchange.wallet_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for withdrawing gold from a wallet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawGoldRequestDTO {
    /**
     * The quantity of gold to withdraw (in grams). Must be positive.
     */
    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be greater than 0")
    // @JsonDeserialize(using = NumberDeserializers.IntegerDeserializer.class) //
    // Ensures only integer values are accepted
    private Integer quantity;
}
