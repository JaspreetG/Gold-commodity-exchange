package io.goldexchange.wallet_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.goldexchange.wallet_service.dto.AddMoneyRequest;
import io.goldexchange.wallet_service.dto.WithdrawMoneyRequest;
import io.goldexchange.wallet_service.model.Wallet;
import io.goldexchange.wallet_service.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import io.goldexchange.wallet_service.dto.WalletDTO;


import java.util.Map;

@RestController
@RequestMapping("api/wallet")
@PreAuthorize("isAuthenticated()")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @PostMapping("/createWallet")
    public ResponseEntity<?> createWallet(Authentication authentication, HttpServletRequest request) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: User not authenticated"));
        }

        Long userId = (Long) authentication.getPrincipal();

        // Check if wallet already exists
        WalletDTO existingWallet = walletService.getWallet(userId);
        if (existingWallet != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Wallet already exists"));
        }

        // Create new wallet
        WalletDTO wallet = walletService.createWallet(userId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Wallet created successfully", "wallet", wallet));
    }

    @GetMapping("/getWallet")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWallet(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        Long userId = (Long) authentication.getPrincipal();
        WalletDTO wallet = walletService.getWallet(userId);

        if (wallet == null) {
            return ResponseEntity.status(404).body(java.util.Map.of("error", "Wallet not found"));
        }

        return ResponseEntity.ok(java.util.Map.of("wallet", wallet));
    }

    @PostMapping("/addMoney")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addMoney(@RequestBody AddMoneyRequest req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        Long userId = (Long) authentication.getPrincipal();
        // Use walletService to add money
        walletService.addMoney(userId, req.getAmount());
        return ResponseEntity.ok(java.util.Map.of("message", "Money added successfully"));
    }

    @PostMapping("/withdrawMoney")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdrawMoney(@RequestBody WithdrawMoneyRequest req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        Long userId = (Long) authentication.getPrincipal();
        // Use walletService to withdraw money
        try {
            walletService.withdrawMoney(userId, req.getAmount());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Map.of("error", e.getMessage()));
        }
        return ResponseEntity.ok(java.util.Map.of("message", "Money withdrawn successfully"));
    }
}
