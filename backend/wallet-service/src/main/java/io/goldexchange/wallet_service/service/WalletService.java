package io.goldexchange.wallet_service.service;

import io.goldexchange.wallet_service.dto.TradeDTO;
import io.goldexchange.wallet_service.dto.WalletDTO;

public interface WalletService {

    WalletDTO getWallet(Long userId);

    WalletDTO createWallet(Long userId);

    void addMoney(Long userId, Double amount);

    void withdrawMoney(Long userId, Double amount);

    void updateWallets(TradeDTO tradeDTO);

    void addGold(Long userId, int quantity);
    void withdrawGold(Long userId, int quantity);

}