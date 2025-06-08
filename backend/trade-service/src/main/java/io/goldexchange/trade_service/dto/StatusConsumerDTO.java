
package io.goldexchange.trade_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusConsumerDTO {

    private String orderId;
    private String userId;
    private String side; // buyer/seller
    private int quantity; 
   
}

