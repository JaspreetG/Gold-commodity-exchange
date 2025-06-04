package io.goldexchange.wallet_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeDTO {

    private String buyOrderId;  //buyer
    private String sellOrderId;  // seller
    private Double price;
    private int quantity;

}



