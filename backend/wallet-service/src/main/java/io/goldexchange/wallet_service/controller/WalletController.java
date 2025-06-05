package io.goldexchange.wallet_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.goldexchange.wallet_service.dto.AddGoldRequestDTO;
import io.goldexchange.wallet_service.dto.AddMoneyRequest;
import io.goldexchange.wallet_service.dto.TradeDTO;
import io.goldexchange.wallet_service.dto.WithdrawMoneyRequest;
import io.goldexchange.wallet_service.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import io.goldexchange.wallet_service.dto.WalletDTO;
import io.goldexchange.wallet_service.dto.WithdrawGoldRequestDTO;

import java.util.Map;

@RestController
@RequestMapping("api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Value("${internal.secret.token}")
    private String internalSecretToken;

    @PostMapping("/createWallet")
    @PreAuthorize("isAuthenticated()")
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
        System.out.println("get wallet bja");
        WalletDTO wallet = walletService.getWallet(userId);

        if (wallet == null) {
            return ResponseEntity.status(404).body(java.util.Map.of("error", "Wallet not found"));
        }
        System.out.println(wallet);
        return ResponseEntity.ok(wallet);
    }

    // for callby saveTrade() in service of trade to get wallet of another user with
    // whom trade has happend
    @PostMapping("/internal/updateWallets")
    public ResponseEntity<?> updateWallets(@RequestHeader("X-Internal-Secret") String internalSecret, @RequestBody TradeDTO tradeDTO) {

        // Validate internal service secret
        try{
            System.out.println("*********IN UPDATE WALLETS WALLET SERVICE*********");
            if (!(internalSecretToken.equals(internalSecret))) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized internal call");
            }
            System.out.println("*********PASSED*********");

            walletService.updateWallets(tradeDTO);
            return ResponseEntity.ok(Map.of("message", "wallet updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Exception in updating wallets: " + e.getMessage()));
        }
    }

    @PostMapping("/addMoney")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addMoney(@Valid @RequestBody AddMoneyRequest req, Authentication authentication) {
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
    public ResponseEntity<?> withdrawMoney(@Valid @RequestBody WithdrawMoneyRequest req, Authentication authentication) {
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

    @PostMapping("/addGold")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addGold(@Valid @RequestBody AddGoldRequestDTO req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        // System.out.println("Entered");
        Long userId = (Long) authentication.getPrincipal();
        // Use walletService to add money
        walletService.addGold(userId, req.getQuantity());
        return ResponseEntity.ok(java.util.Map.of("message", "Gold added successfully"));
    }

    @PostMapping("/withdrawGold")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdrawGold(@Valid @RequestBody WithdrawGoldRequestDTO req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        Long userId = (Long) authentication.getPrincipal();
        // Use walletService to add money
        walletService.withdrawGold(userId, req.getQuantity());
        return ResponseEntity.ok(java.util.Map.of("message", "Gold withdrawn successfully"));
    }
}
