package io.goldexchange.wallet_service.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // @Override
    // public WalletDTO createWallet(Long userId){
    // Wallet wallet = new Wallet();
    // wallet.setUserId(userId);
    // wallet.setBalance(0.0);
    // wallet.setGold(0.0);
    // wallet=walletRepository.save(wallet);

    // WalletDTO walletDTO=new WalletDTO();
    // BeanUtils.copyProperties(wallet, walletDTO);
    // return walletDTO;
    // }
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
}
