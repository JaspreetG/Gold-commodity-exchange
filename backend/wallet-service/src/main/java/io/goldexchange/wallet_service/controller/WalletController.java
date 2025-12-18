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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * REST Controller for Wallet services.
 * Handles wallet creation, retrieval, updates, and transactions.
 */
@RestController
@RequestMapping("api/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

    @Value("${internal.secret.token:mySecretToken}")
    private String internalSecretToken;

    /**
     * Creates a new wallet for the authenticated user.
     *
     * @param authentication The authentication object.
     * @param request        The HTTP request.
     * @return A response entity with the created wallet details.
     */
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
            logger.warn("Wallet creation attempt failed: Wallet already exists for user {}", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Wallet already exists"));
        }

        // Create new wallet
        WalletDTO wallet = walletService.createWallet(userId);

        logger.info("Wallet created successfully for user {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Wallet created successfully", "wallet", wallet));
    }

    /**
     * Retrieves the wallet details for the authenticated user.
     *
     * @param authentication The authentication object.
     * @return The wallet details.
     */
    @GetMapping("/getWallet")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getWallet(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        Long userId = (Long) authentication.getPrincipal();

        WalletDTO wallet = walletService.getWallet(userId);

        if (wallet == null) {
            logger.warn("Wallet not found for user {}", userId);
            return ResponseEntity.status(404).body(java.util.Map.of("error", "Wallet not found"));
        }

        logger.debug("Wallet retrieved for user {}", userId);
        return ResponseEntity.ok(wallet);
    }

    /**
     * Updates wallets after a trade. Internal endpoint.
     *
     * @param internalSecret The secret token for internal authorization.
     * @param tradeDTO       The trade details affecting the wallets.
     * @return A success message or error.
     */
    @PostMapping("/internal/updateWallets")
    public ResponseEntity<?> updateWallets(@RequestHeader("X-Internal-Secret") String internalSecret, @RequestBody TradeDTO tradeDTO) {

        // Validate internal service secret
        try{
            if (!(internalSecretToken.equals(internalSecret))) {
                logger.warn("Unauthorized internal wallet update attempt");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized internal call");
            }

            logger.info("Updating wallets for trade between users {} and {}", tradeDTO.getBuyUserId(), tradeDTO.getSellUserId());
            walletService.updateWallets(tradeDTO);
            return ResponseEntity.ok(Map.of("message", "wallet updated successfully"));
        } catch (Exception e) {
            logger.error("Exception in updating wallets: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Exception in updating wallets: " + e.getMessage()));
        }
    }

    /**
     * Adds money to the authenticated user's wallet.
     *
     * @param req            The request containing the amount to add.
     * @param authentication The authentication object.
     * @return A success message.
     */
    @PostMapping("/addMoney")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addMoney(@Valid @RequestBody AddMoneyRequest req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        Long userId = (Long) authentication.getPrincipal();
        // Use walletService to add money
        walletService.addMoney(userId, req.getAmount());
        logger.info("Added money {} for user {}", req.getAmount(), userId);
        return ResponseEntity.ok(java.util.Map.of("message", "Money added successfully"));
    }

    /**
     * Withdraws money from the authenticated user's wallet.
     *
     * @param req            The request containing the amount to withdraw.
     * @param authentication The authentication object.
     * @return A success message or error.
     */
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
            logger.warn("Withdrawal failed for user {}: {}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(java.util.Map.of("error", e.getMessage()));
        }
        logger.info("Withdrawn money {} for user {}", req.getAmount(), userId);
        return ResponseEntity.ok(java.util.Map.of("message", "Money withdrawn successfully"));
    }

    /**
     * Adds gold to the authenticated user's wallet.
     *
     * @param req            The request containing the quantity of gold to add.
     * @param authentication The authentication object.
     * @return A success message.
     */
    @PostMapping("/addGold")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addGold(@Valid @RequestBody AddGoldRequestDTO req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }

        Long userId = (Long) authentication.getPrincipal();
        // Use walletService to add money
        walletService.addGold(userId, req.getQuantity());
        logger.info("Added gold {} for user {}", req.getQuantity(), userId);
        return ResponseEntity.ok(java.util.Map.of("message", "Gold added successfully"));
    }

    /**
     * Withdraws gold from the authenticated user's wallet.
     *
     * @param req            The request containing the quantity of gold to withdraw.
     * @param authentication The authentication object.
     * @return A success message.
     */
    @PostMapping("/withdrawGold")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> withdrawGold(@Valid @RequestBody WithdrawGoldRequestDTO req, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).body(java.util.Map.of("redirect", "/login"));
        }
        Long userId = (Long) authentication.getPrincipal();
        // Use walletService to add money
        walletService.withdrawGold(userId, req.getQuantity());
        logger.info("Withdrawn gold {} for user {}", req.getQuantity(), userId);
        return ResponseEntity.ok(java.util.Map.of("message", "Gold withdrawn successfully"));
    }
}
