package io.goldexchange.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for TOTP Verification Request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyTotpRequest {

    /**
     * The phone number of the user. Must be 10 digits.
     */
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    /**
     * The 6-digit TOTP code.
     */
    @NotBlank(message = "TOTP is required")
    @Pattern(regexp = "\\d{6}", message = "TOTP must be a 6-digit code")
    private String totp;

    /**
     * The unique device fingerprint for security.
     */
    @NotBlank(message = "Device fingerprint is required")
    private String deviceFingerprint;
}
