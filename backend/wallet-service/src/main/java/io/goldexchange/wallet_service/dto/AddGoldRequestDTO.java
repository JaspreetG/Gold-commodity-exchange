package io.goldexchange.wallet_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.Positive;


import jakarta.validation.constraints.NotNull;


import lombok.AllArgsConstructor;

/**
 * DTO for adding gold to a wallet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGoldRequestDTO {
    /**
     * The quantity of gold to add (in grams). Must be positive.
     */
    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be greater than 0")
    // @JsonDeserialize(using = NumberDeserializers.IntegerDeserializer.class) // Ensures only integer values are accepted
    private Integer quantity;
}
