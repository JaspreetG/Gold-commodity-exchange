package io.goldexchange.wallet_service.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.goldexchange.wallet_service.dto.TradeDTO;
import io.goldexchange.wallet_service.dto.WalletDTO;
import io.goldexchange.wallet_service.model.Wallet;
import io.goldexchange.wallet_service.repository.WalletRepositoryWrapper;

@Service
public class WalletServiceImpl implements WalletService {
    @Autowired
    private WalletRepositoryWrapper walletRepository;

    // @Override
    // public WalletDTO getWallet(Long userId) {
    // Wallet wallet = walletRepository.findByUserId(userId);
    // WalletDTO walletDTO = new WalletDTO();
    // BeanUtils.copyProperties(wallet, walletDTO);
    // return walletDTO;
    // }

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

    @Override
    public WalletDTO createWallet(Long userId) {
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(0.0);
        wallet.setGold(0.0);

        Wallet savedWallet = walletRepository.save(wallet);

        // if (savedWallet == null) {
        //     throw new IllegalStateException("Failed to save wallet for user: " + userId);
        // }

        WalletDTO walletDTO = new WalletDTO();
        BeanUtils.copyProperties(savedWallet, walletDTO);
        return walletDTO;
    }

    @Override
    public void updateWallets(TradeDTO tradeDTO) {
        // Assuming tradeDTO contains userId and amount for both users involved in the trade
        Long buyUserId = Long.parseLong(tradeDTO.getBuyOrderId());
        Long sellUserId = Long.parseLong(tradeDTO.getSellOrderId());
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
