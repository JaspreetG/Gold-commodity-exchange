package io.goldexchange.wallet_service.repository;

import io.goldexchange.wallet_service.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepositoryWrapper extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long userId);
}



