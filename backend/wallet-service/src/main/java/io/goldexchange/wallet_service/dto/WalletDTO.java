package io.goldexchange.wallet_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletDTO {
    
    private Long walletId;
    private Long userId;
    private Double balance;
    private Double Gold;
}
