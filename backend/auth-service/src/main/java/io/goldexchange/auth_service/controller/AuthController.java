package io.goldexchange.auth_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;

import io.goldexchange.auth_service.model.User;
import io.goldexchange.auth_service.service.AuthService;
import io.goldexchange.auth_service.dto.VerifyTotpRequest;
import io.goldexchange.auth_service.dto.RegisterRequest;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String phoneNumber = user.getPhoneNumber();

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone number is required"));
        }

        boolean exists = authService.userExistsByPhone(phoneNumber);
        if (!exists) {
            return ResponseEntity.ok(Map.of("redirect", "register"));
        }
        // If user exists, proceed with login (dummy response for now)
        return ResponseEntity.ok(Map.of("message", "user is in DB"));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody VerifyTotpRequest request, HttpServletResponse response) {
        String phoneNumber = request.getPhoneNumber();
        String totp = request.getTotp();
        String deviceFingerprint = request.getDeviceFingerprint();
        

        if (phoneNumber == null || totp == null || deviceFingerprint == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing required fields"));
        }

        User user = authService.getUserByPhone(phoneNumber);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "User not found"));
        }

        

        String secretKey = user.getSecretKey();
        boolean isTotpValid = authService.verifyTotp(secretKey, totp,user);
        if (!isTotpValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Invalid TOTP"));
        }
        
        String jwt = authService.generateJwt(user.getUserId(), deviceFingerprint);
        ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
            .httpOnly(true)
            .path("/")
            .sameSite("Strict")
            .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(Map.of("message", "Login successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String userName = request.getUserName();
        String phoneNumber = request.getPhoneNumber();
        if (userName == null || userName.isEmpty() || phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and phone number are required"));
        }
        boolean exists = authService.userExistsByPhone(phoneNumber);
        if (exists) {
            return ResponseEntity.badRequest().body(Map.of("redirect", "login", "error", "Phone number already registered"));
        }
        // Generate a new secret key for TOTP
        String secretKey = authService.generateSecretKey();
        // Save user with state 'temporary'
        //made it uncatched
        authService.saveUser(userName, phoneNumber, secretKey);
        // Generate QR code for the secret key
        String qrCodeData = authService.generateQrCode(userName, secretKey);
        return ResponseEntity.ok(Map.of("qrCode", qrCodeData, "secretKey", secretKey));
    }
}
