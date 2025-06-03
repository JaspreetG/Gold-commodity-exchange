package io.goldexchange.trade_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data   
@AllArgsConstructor
@NoArgsConstructor
public class AuthCredentials {
    private String jwt;
    private String fingerprint;
}
