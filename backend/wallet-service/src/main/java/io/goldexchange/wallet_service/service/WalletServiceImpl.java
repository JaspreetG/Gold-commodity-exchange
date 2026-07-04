package io.goldexchange.wallet_service.service;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.goldexchange.wallet_service.dto.TradeDTO;
import io.goldexchange.wallet_service.dto.WalletDTO;
import io.goldexchange.wallet_service.model.Wallet;
import io.goldexchange.wallet_service.repository.WalletRepositoryWrapper;

/**
 * Implementation of the {@link WalletService} interface.
 * Handles core business logic for wallet operations such as creation, retrieving balances, 
 * adding/withdrawing funds and gold, as well as orchestrating transaction processing for trades.
 */
@Service
public class WalletServiceImpl implements WalletService {
    
    /**
     * Data access wrapper for wallet entities.
     */
    private final WalletRepositoryWrapper walletRepository;

    /**
     * Constructs a new WalletServiceImpl with the required repository dependency.
     * 
     * @param walletRepository The repository wrapper used for database operations related to wallets.
     */
    public WalletServiceImpl(WalletRepositoryWrapper walletRepository) {
        this.walletRepository = walletRepository;
    }

    /**
     * Retrieves the wallet details for a specified user.
     *
     * @param userId The unique identifier of the user whose wallet is to be retrieved.
     * @return A WalletDTO containing the user's wallet information, or null if the wallet does not exist.
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
     * Creates and initializes a new wallet for a user with zero balance and zero gold.
     * This is typically called when a user registers or requires a new wallet.
     *
     * @param userId The unique identifier of the user for whom the wallet will be created.
     * @return A WalletDTO representing the newly created wallet.
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
        return walletDTO;
    }

    /**
     * Executes the financial portion of a trade by updating the wallets of both the buyer and seller.
     * The operation is transactional to ensure that money and gold are transferred atomically.
     *
     * @param tradeDTO The data transfer object containing the details of the trade (buyer ID, seller ID, price, quantity).
     */
    @Override
    @Transactional
    public void updateWallets(TradeDTO tradeDTO) {
        // Assuming tradeDTO contains userId and amount for both users involved in the trade
        Long buyUserId = Long.parseLong(tradeDTO.getBuyUserId());
        Long sellUserId = Long.parseLong(tradeDTO.getSellUserId());
        Double price = tradeDTO.getPrice();
        int quantity=tradeDTO.getQuantity();

        double totalAmount = price * quantity;

        //BuyUser's wallet
        withdrawMoney(buyUserId, totalAmount);
        addGold(buyUserId, quantity);   

        // SellUser's wallet
        addMoney(sellUserId, totalAmount);
        withdrawGold(sellUserId, quantity);

    }

    /**
     * Adds a specified monetary amount to a user's wallet balance.
     * This method runs within a transaction to maintain consistency.
     *
     * @param userId The unique identifier of the user.
     * @param amount The amount of money to add. Must be greater than zero.
     * @throws IllegalArgumentException If the specified user's wallet is not found.
     */
    @Override
    @Transactional
    public void addMoney(Long userId, Double amount) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet != null) {
            wallet.setBalance(wallet.getBalance() + amount);
            walletRepository.save(wallet);
        } else {
           throw new IllegalArgumentException("Wallet not found for user: " + userId);
        }
    }

    /**
     * Deducts a specified monetary amount from a user's wallet balance.
     * It ensures the user has sufficient balance before proceeding to prevent overdrafts.
     *
     * @param userId The unique identifier of the user.
     * @param amount The amount of money to withdraw.
     * @throws IllegalArgumentException If the wallet is not found or if the user has insufficient balance.
     */
    @Override
    @Transactional
    public void withdrawMoney(Long userId, Double amount) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet != null && wallet.getBalance() >= amount) {
            wallet.setBalance(wallet.getBalance() - amount);
            walletRepository.save(wallet);
        } else {
            throw new IllegalArgumentException("Insufficient balance");
        }
    }

    /**
     * Credits a specified quantity of gold to a user's wallet.
     * This operation is primarily used when a user buys gold or makes a deposit.
     *
     * @param userId   The unique identifier of the user.
     * @param quantity The quantity of gold to add.
     * @throws IllegalArgumentException If the specified user's wallet is not found.
     */
    @Override
    @Transactional
    public void addGold(Long userId, int quantity) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet != null) {
            wallet.setGold(wallet.getGold() + quantity);
            walletRepository.save(wallet);
        } else {
            throw new IllegalArgumentException("Wallet not found for user: " + userId);
        }
    }

    /**
     * Deducts a specified quantity of gold from a user's wallet.
     * Ensures the user has sufficient gold to complete the withdrawal to prevent negative balances.
     *
     * @param userId   The unique identifier of the user.
     * @param quantity The quantity of gold to withdraw.
     * @throws IllegalArgumentException If the wallet is not found or if there is insufficient gold in the wallet.
     */
    @Override
    @Transactional
    public void withdrawGold(Long userId, int quantity) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet != null && wallet.getGold() >= quantity) {
            wallet.setGold(wallet.getGold() - quantity);
            walletRepository.save(wallet);
        } else {
            throw new IllegalArgumentException("Insufficient gold");
        }
    }

}
