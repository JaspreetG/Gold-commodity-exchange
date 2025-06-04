package io.goldexchange.trade_service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Side is required")
    @Pattern(regexp = "BUY|SELL", message = "Side must be either BUY or SELL")
    private String side; // BUY or SELL

    @NotNull(message = "Type is required")
    @Pattern(regexp = "LIMIT|MARKET", message = "Type must be either LIMIT or MARKET")
    private String type; // LIMIT or MARKET
}