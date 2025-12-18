package io.goldexchange.wallet_service.service;

import io.goldexchange.wallet_service.dto.TradeDTO;
import io.goldexchange.wallet_service.dto.WalletDTO;

/**
 * Interface for Wallet Service.
 * Defines methods for wallet management and transactions.
 */
public interface WalletService {

    /**
     * Retrieves the wallet for a given user.
     *
     * @param userId The user ID.
     * @return The WalletDTO containing wallet details.
     */
    WalletDTO getWallet(Long userId);

    /**
     * Creates a new wallet for a user.
     *
     * @param userId The user ID.
     * @return The created WalletDTO.
     */
    WalletDTO createWallet(Long userId);

    /**
     * Adds money to a user's wallet.
     *
     * @param userId The user ID.
     * @param amount The amount to add.
     */
    void addMoney(Long userId, Double amount);

    /**
     * Withdraws money from a user's wallet.
     *
     * @param userId The user ID.
     * @param amount The amount to withdraw.
     */
    void withdrawMoney(Long userId, Double amount);

    /**
     * Updates wallets for both parties in a trade.
     *
     * @param tradeDTO The trade details.
     */
    void updateWallets(TradeDTO tradeDTO);

    /**
     * Adds gold to a user's wallet.
     *
     * @param userId   The user ID.
     * @param quantity The quantity of gold to add.
     */
    void addGold(Long userId, int quantity);

    /**
     * Withdraws gold from a user's wallet.
     *
     * @param userId   The user ID.
     * @param quantity The quantity of gold to withdraw.
     */
    void withdrawGold(Long userId, int quantity);

}