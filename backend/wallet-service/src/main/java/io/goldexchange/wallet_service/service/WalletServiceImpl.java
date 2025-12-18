package io.goldexchange.wallet_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.goldexchange.wallet_service.dto.TradeDTO;
import io.goldexchange.wallet_service.dto.WalletDTO;
import io.goldexchange.wallet_service.model.Wallet;
import io.goldexchange.wallet_service.repository.WalletRepositoryWrapper;

/**
 * Implementation of the WalletService.
 * Handles wallet creation, retrieval, and updating balances (money and gold).
 */
@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Autowired
    private WalletRepositoryWrapper walletRepository;

    /**
     * Retrieves a wallet by user ID.
     *
     * @param userId The ID of the user.
     * @return The WalletDTO, or null if not found.
     */
    @Override
    public WalletDTO getWallet(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);

        if (wallet == null) {
            return null;
        }

        WalletDTO walletDTO = new WalletDTO();
        BeanUtils.copyProperties(wallet, walletDTO);
        return walletDTO;
    }

    /**
     * Creates a new wallet for a user.
     *
     * @param userId The ID of the user.
     * @return The created WalletDTO.
     */
    @Override
    public WalletDTO createWallet(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(0.0);
        wallet.setGold(0.0);

        Wallet savedWallet = walletRepository.save(wallet);

        WalletDTO walletDTO = new WalletDTO();
        BeanUtils.copyProperties(savedWallet, walletDTO);
        logger.info("Wallet created for user ID: {}", userId);
        return walletDTO;
    }

    /**
     * Updates wallets after a trade has occurred.
     *
     * @param tradeDTO Data transfer object containing trade details.
     */
    @Override
    public void updateWallets(TradeDTO tradeDTO) {
        try {
            Long buyUserId = Long.parseLong(tradeDTO.getBuyUserId());
            Long sellUserId = Long.parseLong(tradeDTO.getSellUserId());
            Double price = tradeDTO.getPrice();
            int quantity = tradeDTO.getQuantity();

            double totalAmount = price * quantity;

            // Update Buyer's wallet
            withdrawMoney(buyUserId, totalAmount);
            addGold(buyUserId, quantity);

            // Update Seller's wallet
            addMoney(sellUserId, totalAmount);
            withdrawGold(sellUserId, quantity);

            logger.info("Wallets updated for trade: {}", tradeDTO);
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format in TradeDTO", e);
            throw e;
        } catch (Exception e) {
            logger.error("Failed to update wallets for trade", e);
            throw e;
        }
    }

    /**
     * Adds money to a user's wallet.
     *
     * @param userId The ID of the user.
     * @param amount The amount to add.
     */
    @Override
    @Transactional
    public void addMoney(Long userId, Double amount) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.save(wallet);
            logger.debug("Added {} to user {} wallet", amount, userId);
        } else {
            logger.error("Wallet not found for user: {}", userId);
            throw new IllegalArgumentException("Wallet not found for user: " + userId);
        }
    }

    /**
     * Withdraws money from a user's wallet.
     *
     * @param userId The ID of the user.
     * @param amount The amount to withdraw.
     */
    @Override
    @Transactional
    public void withdrawMoney(Long userId, Double amount) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet != null && wallet.getBalance() >= amount) {
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
            logger.debug("Withdrew {} from user {} wallet", amount, userId);
        } else {
            logger.error("Insufficient balance for user: {}", userId);
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

    /**
     * Adds gold to a user's wallet.
     *
     * @param userId   The ID of the user.
     * @param quantity The quantity of gold to add.
     */
    @Override
    @Transactional
    public void addGold(Long userId, int quantity) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet != null) {
            wallet.setGold(wallet.getGold() + quantity);
            walletRepository.save(wallet);
            logger.debug("Added {} gold to user {} wallet", quantity, userId);
        } else {
            logger.error("Wallet not found for user: {}", userId);
            throw new IllegalArgumentException("Wallet not found for user: " + userId);
        }
    }

    /**
     * Withdraws gold from a user's wallet.
     *
     * @param userId   The ID of the user.
     * @param quantity The quantity of gold to withdraw.
     */
    @Override
    @Transactional
    public void withdrawGold(Long userId, int quantity) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet != null && wallet.getGold() >= quantity) {
            wallet.setGold(wallet.getGold() - quantity);
            walletRepository.save(wallet);
            logger.debug("Withdrew {} gold from user {} wallet", quantity, userId);
        } else {
            logger.error("Insufficient gold for user: {}", userId);
            throw new IllegalArgumentException("Insufficient gold");
        }
    }

}