package io.goldexchange.wallet_service.repository;

import io.goldexchange.wallet_service.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Wallet entity operations.
 */
@Repository
public interface WalletRepositoryWrapper extends JpaRepository<Wallet, Long> {
    /**
     * Finds a wallet by user ID.
     *
     * @param userId The ID of the user.
     * @return The Wallet entity associated with the user.
     */
    Wallet findByUserId(Long userId);
}



