package io.goldexchange.wallet_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor  
@AllArgsConstructor
public class WithdrawMoneyRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private Double amount;  
}
