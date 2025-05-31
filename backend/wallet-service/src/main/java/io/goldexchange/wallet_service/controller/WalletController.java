package io.goldexchange.wallet_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.goldexchange.wallet_service.dto.AddMoneyRequest;
import io.goldexchange.wallet_service.dto.WithdrawMoneyRequest;
import io.goldexchange.wallet_service.model.Wallet;
import io.goldexchange.wallet_service.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("api/wallet")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping("/getWallet")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWallet(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        Long userId = (Long) authentication.getPrincipal();
        Wallet wallet = walletService.getWallet(userId);

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
        walletService.withdrawMoney(userId, req.getAmount());
        return ResponseEntity.ok(java.util.Map.of("message", "Money withdrawn successfully"));
    }
}
