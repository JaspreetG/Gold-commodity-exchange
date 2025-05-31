package io.goldexchange.auth_service.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class OtpAuthenticationToken extends AbstractAuthenticationToken {
    private final String phoneNumber;
    private final String totp;

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