package io.goldexchange.wallet_service.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.goldexchange.wallet_service.dto.TradeDTO;
import io.goldexchange.wallet_service.dto.WalletDTO;
import io.goldexchange.wallet_service.model.Wallet;
import io.goldexchange.wallet_service.repository.WalletRepositoryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepositoryWrapper walletRepository;

    @InjectMocks
    private WalletServiceImpl walletService;

    private Wallet buyerWallet;
    private Wallet sellerWallet;

    private final Long buyerUserId = 1L;
    private final Long sellerUserId = 2L;
    private final Long buyerWalletId = 10L;
    private final Long sellerWalletId = 20L;

    @BeforeEach
    void setUp() {
        buyerWallet = new Wallet(buyerWalletId, buyerUserId, 1000.0, 0.0);
        sellerWallet = new Wallet(sellerWalletId, sellerUserId, 500.0, 10.0);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // getWallet
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void getWallet_whenFound_returnsWalletDTO() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        WalletDTO result = walletService.getWallet(buyerUserId);

        assertThat(result).isNotNull();
        assertThat(result.getWalletId()).isEqualTo(buyerWalletId);
        assertThat(result.getUserId()).isEqualTo(buyerUserId);
        assertThat(result.getBalance()).isEqualTo(1000.0);
        assertThat(result.getGold()).isEqualTo(0.0);
    }

    @Test
    void getWallet_whenNotFound_returnsNull() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(null);

        WalletDTO result = walletService.getWallet(buyerUserId);

        assertThat(result).isNull();
    }

    // ──────────────────────────────────────────────────────────────────────────
    // createWallet
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void createWallet_createsWithZeroBalanceAndZeroGold() {
        Wallet savedWallet = new Wallet(buyerWalletId, buyerUserId, 0.0, 0.0);
        when(walletRepository.save(any(Wallet.class))).thenReturn(savedWallet);

        WalletDTO result = walletService.createWallet(buyerUserId);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(buyerUserId);
        assertThat(result.getBalance()).isEqualTo(0.0);
        assertThat(result.getGold()).isEqualTo(0.0);

        ArgumentCaptor<Wallet> captor = ArgumentCaptor.forClass(Wallet.class);
        verify(walletRepository).save(captor.capture());
        Wallet passed = captor.getValue();
        assertThat(passed.getUserId()).isEqualTo(buyerUserId);
        assertThat(passed.getBalance()).isEqualTo(0.0);
        assertThat(passed.getGold()).isEqualTo(0.0);
    }

    // ──────────────────────────────────────────────────────────────────────────
    // updateWallets
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void updateWallets_successfulTransfer() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);
        when(walletRepository.findByUserId(sellerUserId)).thenReturn(sellerWallet);

        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setBuyUserId(buyerUserId.toString());
        tradeDTO.setSellUserId(sellerUserId.toString());
        tradeDTO.setPrice(100.0);
        tradeDTO.setQuantity(5);

        walletService.updateWallets(tradeDTO);

        // buyer pays 500 money, gets 5 gold
        assertThat(buyerWallet.getBalance()).isEqualTo(500.0);
        assertThat(buyerWallet.getGold()).isEqualTo(5.0);

        // seller gets 500 money, loses 5 gold
        assertThat(sellerWallet.getBalance()).isEqualTo(1000.0);
        assertThat(sellerWallet.getGold()).isEqualTo(5.0);

        verify(walletRepository, times(4)).save(any(Wallet.class));
    }

    @Test
    void updateWallets_insufficientBuyerBalance_throwsException() {
        buyerWallet.setBalance(50.0);
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setBuyUserId(buyerUserId.toString());
        tradeDTO.setSellUserId(sellerUserId.toString());
        tradeDTO.setPrice(100.0);
        tradeDTO.setQuantity(1);

        assertThatThrownBy(() -> walletService.updateWallets(tradeDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void updateWallets_buyerWalletNotFound_throwsException() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(null);

        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setBuyUserId(buyerUserId.toString());
        tradeDTO.setSellUserId(sellerUserId.toString());
        tradeDTO.setPrice(100.0);
        tradeDTO.setQuantity(1);

        assertThatThrownBy(() -> walletService.updateWallets(tradeDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void updateWallets_sellerWalletNotFound_throwsException() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);
        when(walletRepository.findByUserId(sellerUserId)).thenReturn(null);

        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setBuyUserId(buyerUserId.toString());
        tradeDTO.setSellUserId(sellerUserId.toString());
        tradeDTO.setPrice(100.0);
        tradeDTO.setQuantity(1);

        assertThatThrownBy(() -> walletService.updateWallets(tradeDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wallet not found for user");
    }

    @Test
    void updateWallets_insufficientSellerGold_throwsException() {
        sellerWallet.setGold(2.0);
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);
        when(walletRepository.findByUserId(sellerUserId)).thenReturn(sellerWallet);

        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setBuyUserId(buyerUserId.toString());
        tradeDTO.setSellUserId(sellerUserId.toString());
        tradeDTO.setPrice(100.0);
        tradeDTO.setQuantity(5);

        assertThatThrownBy(() -> walletService.updateWallets(tradeDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient gold");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // addMoney
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void addMoney_success() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        walletService.addMoney(buyerUserId, 250.0);

        assertThat(buyerWallet.getBalance()).isEqualTo(1250.0);
        verify(walletRepository).save(buyerWallet);
    }

    @Test
    void addMoney_walletNotFound_throws() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(null);

        assertThatThrownBy(() -> walletService.addMoney(buyerUserId, 100.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wallet not found for user");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // withdrawMoney
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void withdrawMoney_success() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        walletService.withdrawMoney(buyerUserId, 300.0);

        assertThat(buyerWallet.getBalance()).isEqualTo(700.0);
        verify(walletRepository).save(buyerWallet);
    }

    @Test
    void withdrawMoney_insufficientBalance_throws() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        assertThatThrownBy(() -> walletService.withdrawMoney(buyerUserId, 2000.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void withdrawMoney_walletNotFound_throws() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(null);

        assertThatThrownBy(() -> walletService.withdrawMoney(buyerUserId, 50.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient balance");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // addGold
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void addGold_success() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        walletService.addGold(buyerUserId, 7);

        assertThat(buyerWallet.getGold()).isEqualTo(7.0);
        verify(walletRepository).save(buyerWallet);
    }

    @Test
    void addGold_walletNotFound_throws() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(null);

        assertThatThrownBy(() -> walletService.addGold(buyerUserId, 5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Wallet not found for user");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // withdrawGold
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void withdrawGold_success() {
        when(walletRepository.findByUserId(sellerUserId)).thenReturn(sellerWallet);

        walletService.withdrawGold(sellerUserId, 4);

        assertThat(sellerWallet.getGold()).isEqualTo(6.0);
        verify(walletRepository).save(sellerWallet);
    }

    @Test
    void withdrawGold_insufficientGold_throws() {
        when(walletRepository.findByUserId(sellerUserId)).thenReturn(sellerWallet);

        assertThatThrownBy(() -> walletService.withdrawGold(sellerUserId, 20))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient gold");
    }

    @Test
    void withdrawGold_walletNotFound_throws() {
        when(walletRepository.findByUserId(sellerUserId)).thenReturn(null);

        assertThatThrownBy(() -> walletService.withdrawGold(sellerUserId, 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient gold");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Edge cases
    // ──────────────────────────────────────────────────────────────────────────

    @Test
    void addMoney_withZeroAmount_success() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        walletService.addMoney(buyerUserId, 0.0);

        assertThat(buyerWallet.getBalance()).isEqualTo(1000.0);
        verify(walletRepository).save(buyerWallet);
    }

    @Test
    void withdrawMoney_exactBalance_success() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        walletService.withdrawMoney(buyerUserId, 1000.0);

        assertThat(buyerWallet.getBalance()).isEqualTo(0.0);
        verify(walletRepository).save(buyerWallet);
    }

    @Test
    void addGold_withZeroQuantity_success() {
        when(walletRepository.findByUserId(buyerUserId)).thenReturn(buyerWallet);

        walletService.addGold(buyerUserId, 0);

        assertThat(buyerWallet.getGold()).isEqualTo(0.0);
        verify(walletRepository).save(buyerWallet);
    }

    @Test
    void withdrawGold_exactGold_success() {
        when(walletRepository.findByUserId(sellerUserId)).thenReturn(sellerWallet);

        walletService.withdrawGold(sellerUserId, 10);

        assertThat(sellerWallet.getGold()).isEqualTo(0.0);
        verify(walletRepository).save(sellerWallet);
    }
}
