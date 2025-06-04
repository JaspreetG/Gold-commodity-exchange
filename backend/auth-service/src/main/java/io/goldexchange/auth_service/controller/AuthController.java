package io.goldexchange.auth_service.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import io.goldexchange.auth_service.security.OtpAuthenticationToken;
import io.goldexchange.auth_service.service.AuthService;
import io.goldexchange.auth_service.dto.VerifyTotpRequest;
import io.goldexchange.auth_service.model.User;
import io.goldexchange.auth_service.dto.LoginRequest;
import io.goldexchange.auth_service.dto.RegisterRequest;
import io.goldexchange.auth_service.dto.UserDTO;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest user) {
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
    public ResponseEntity<?> verify(@Valid @RequestBody VerifyTotpRequest request, HttpServletResponse response) {
        try {
            OtpAuthenticationToken authRequest = new OtpAuthenticationToken(request.getPhoneNumber(),
                    request.getTotp());
            Authentication authentication = authenticationManager.authenticate(authRequest);
            // If authentication is successful, you can generate JWT and set cookie as
            // before
            User userEntity = (User) authentication.getPrincipal();
            if (userEntity == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "User not found"));
            }
            
            UserDTO user=new UserDTO();
            BeanUtils.copyProperties(userEntity, user);
            
           
            String deviceFingerprint = request.getDeviceFingerprint();
            String jwt = authService.generateJwt(user.getUserId(), deviceFingerprint);

            // create jwt
            authService.createWallet(user, jwt, deviceFingerprint);

            ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                    .httpOnly(true)
                    .path("/")
                    .sameSite("Strict")
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
            return ResponseEntity.ok(Map.of("message", "Login successful", "User", user));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        String userName = request.getUserName();
        String phoneNumber = request.getPhoneNumber();
        if (userName == null || userName.isEmpty() || phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and phone number are required"));
        }
        boolean exists = authService.userExistsByPhone(phoneNumber);
        if (exists) {
            return ResponseEntity.badRequest()
                    .body(Map.of("redirect", "login", "error", "Phone number already registered"));
        }
        // Generate a new secret key for TOTP
        String secretKey = authService.generateSecretKey();
        // Save user with state 'temporary'
        authService.saveUser(userName, phoneNumber, secretKey);
        // Generate QR code for the secret key
        String qrCodeData = authService.generateQrCode(userName, secretKey);
        return ResponseEntity.ok(Map.of("qrCode", qrCodeData, "secretKey", secretKey));
    }
}
