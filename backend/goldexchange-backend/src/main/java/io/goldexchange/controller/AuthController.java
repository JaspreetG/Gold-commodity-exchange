package io.goldexchange.controller;

import io.goldexchange.dto.LoginRequest;
import io.goldexchange.dto.UserResponse;
import io.goldexchange.entity.User;
import io.goldexchange.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        User user = authService.login(request);
        // map User to UserResponse
        return null;
    }
}
