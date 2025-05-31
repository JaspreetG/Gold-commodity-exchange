package io.goldexchange.auth_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import java.util.List;

import io.goldexchange.auth_service.model.User;
import io.goldexchange.auth_service.service.AuthService;

@Component
public class OtpAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phoneNumber = authentication.getPrincipal().toString();
        String totp = authentication.getCredentials().toString();

        User user = authService.getUserByPhone(phoneNumber);
        if (user == null) {
            throw new BadCredentialsException("User not found");
        }

        boolean valid = authService.verifyTotp(user.getSecretKey(), totp, user);
        if (!valid) {
            throw new BadCredentialsException("Invalid TOTP");
        }

        return new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthenticationToken.class.isAssignableFrom(authentication);
    }
}