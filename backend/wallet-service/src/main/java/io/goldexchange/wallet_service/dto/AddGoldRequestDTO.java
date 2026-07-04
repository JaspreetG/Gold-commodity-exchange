package io.goldexchange.wallet_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.Positive;


import jakarta.validation.constraints.NotNull;


import lombok.AllArgsConstructor;

/**
 * Data Transfer Object (DTO) for client requests to deposit gold into their wallet.
 * Incorporates validation annotations to verify that the incoming payload contains 
 * a strictly positive quantity of gold.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddGoldRequestDTO {
    /**
     * The exact quantity of gold (measured in grams) the user wants to add to their holdings.
     * Validated to ensure it is not null and is greater than zero.
     */
    @NotNull(message = "quantity is required")
    @Positive(message = "quantity must be greater than 0")
    // @JsonDeserialize(using = NumberDeserializers.IntegerDeserializer.class) // Ensures only integer values are accepted
    private Integer quantity;
}
