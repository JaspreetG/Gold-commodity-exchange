package io.goldexchange.auth_service.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Custom Authentication Token for OTP-based authentication.
 */
public class OtpAuthenticationToken extends AbstractAuthenticationToken {
    private final String phoneNumber;
    private final String totp;

    /**
     * Constructs an OtpAuthenticationToken.
     *
     * @param phoneNumber The user's phone number.
     * @param totp        The TOTP code.
     */
    public OtpAuthenticationToken(String phoneNumber, String totp) {
        super(null);
        this.phoneNumber = phoneNumber;
        this.totp = totp;
        setAuthenticated(false);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTotp() {
        return totp;
    }

    @Override
    public Object getCredentials() {
        return totp;
    }

    @Override
    public Object getPrincipal() {
        return phoneNumber;
    }
}