package io.goldexchange.wallet_service.repository;

import io.goldexchange.wallet_service.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for standard CRUD and custom database operations on the {@link Wallet} entity.
 * Extends JpaRepository to inherit standard persistence capabilities without boilerplate code.
 */
public interface WalletRepositoryWrapper extends JpaRepository<Wallet, Long> {
    
    /**
     * Retrieves a wallet associated with a specific user.
     * As each user has exactly one wallet, this method acts as a direct lookup to fetch user balances.
     *
     * @param userId The unique identifier of the user who owns the wallet.
     * @return The {@link Wallet} entity if found, otherwise null.
     */
    Wallet findByUserId(Long userId);
}



