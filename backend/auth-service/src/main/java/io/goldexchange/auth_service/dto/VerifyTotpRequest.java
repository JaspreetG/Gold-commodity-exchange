package io.goldexchange.auth_service.dto;

import lombok.Data;

@Data
public class VerifyTotpRequest {
    private String phoneNumber;
    private String totp;
    private String deviceFingerprint;
}
