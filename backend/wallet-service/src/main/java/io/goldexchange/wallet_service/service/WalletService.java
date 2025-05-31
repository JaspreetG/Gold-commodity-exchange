package io.goldexchange.wallet_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.goldexchange.wallet_service.model.Wallet;
import io.goldexchange.wallet_service.repository.WalletRepository;

@Service
public class WalletService {
    @Autowired
    private WalletRepository walletRepository;

    public Wallet getWallet(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);
        return wallet;
    }

    @Transactional
    public void addMoney(Long userId, Double amount) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUserId(userId);
            wallet.setBalance(amount);
        } else {
            wallet.setBalance(wallet.getBalance() + amount);
        }
        walletRepository.save(wallet);
    }

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
}
