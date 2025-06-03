package io.goldexchange.wallet_service.service;

import io.goldexchange.wallet_service.dto.WalletDTO;

public interface WalletService {

    WalletDTO getWallet(Long userId);

    WalletDTO createWallet(Long userId);

    void addMoney(Long userId, Double amount);

    void withdrawMoney(Long userId, Double amount);

    // void updateWalletByUserId(Long userId);

}