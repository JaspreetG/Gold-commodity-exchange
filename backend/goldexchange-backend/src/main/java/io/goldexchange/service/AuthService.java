package io.goldexchange.service;

import io.goldexchange.dto.LoginRequest;
import io.goldexchange.entity.User;
import io.goldexchange.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(LoginRequest request) {
        // Authenticate username/password, verify TOTP, create session
        return null;
    }
}
