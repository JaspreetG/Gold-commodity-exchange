package io.goldexchange.trade_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.goldexchange.trade_service.dto.AuthCredentials;
import io.goldexchange.trade_service.dto.OrderProducerDTO;
import io.goldexchange.trade_service.dto.OrderRequest;
import io.goldexchange.trade_service.dto.PastTradeDTO;
import io.goldexchange.trade_service.dto.TradeConsumerDTO;
import io.goldexchange.trade_service.dto.WalletDTO;
import io.goldexchange.trade_service.model.Trade;
import io.goldexchange.trade_service.producer.OrderProducer;
import io.goldexchange.trade_service.repository.TradeRepository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TradeService {
    private final TradeRepository tradeRepository;
    private final OrderProducer orderProducer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${wallet.service.url}")
    private String walletServiceUrl;

    @Autowired
    private RestTemplate restTemplate;

    public TradeService(OrderProducer orderProducer, TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
        this.orderProducer = orderProducer;
    }

    public void sendOrderToMatcher(OrderRequest orderRequest, Long userId) throws Exception {

        OrderProducerDTO orderProducerDTO = new OrderProducerDTO();
        BeanUtils.copyProperties(orderRequest, orderProducerDTO);
        orderProducerDTO.setUserId(userId.toString());

        String orderJson = objectMapper.writeValueAsString(orderProducerDTO);
        orderProducer.sendOrder(orderJson);
    }

    public void saveTrade(TradeConsumerDTO tradeConsumerDTO) {

        try {

            // buy side
            Trade tradeBuyerSide = new Trade();
            tradeBuyerSide.setUserId(Long.parseLong(tradeConsumerDTO.getBuyOrderId()));
            tradeBuyerSide.setPrice(tradeConsumerDTO.getPrice());
            tradeBuyerSide.setQuantity(tradeConsumerDTO.getQuantity());
            tradeBuyerSide.setSide("BUY");

            tradeRepository.save(tradeBuyerSide);

            // seller side
            Trade tradeSellerSide = new Trade();
            tradeSellerSide.setUserId(Long.parseLong(tradeConsumerDTO.getSellOrderId()));
            tradeSellerSide.setPrice(tradeConsumerDTO.getPrice());
            tradeSellerSide.setQuantity(tradeConsumerDTO.getQuantity());
            tradeSellerSide.setSide("SELL");

            tradeRepository.save(tradeSellerSide);

            // wallet update
            updateWallets(tradeConsumerDTO);

        } catch (Exception e) {
            throw new RuntimeException("Error saving trade: " + e.getMessage(), e);
        }
    }

    public void updateWallets(TradeConsumerDTO tradeConsumerDTO) {
        try {
            String url = walletServiceUrl + "internal/updateWallets"; // ensure this is the correct endpoint
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Internal-Secret", "mySecretToken"); // Use a secure token for internal calls
            headers.setContentType(MediaType.APPLICATION_JSON); // optional, since body is empty

            HttpEntity<TradeConsumerDTO> requestEntity = new HttpEntity<>(tradeConsumerDTO, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Wallet updated successfully.");
            } else {
                System.err.println("Wallet update failed with status: " + response.getStatusCode());
                throw new RuntimeException("Failed to update wallet");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error updating wallets: " + e.getMessage(), e);
        }
    }

    public List<PastTradeDTO> pastTrades(Long userId) {
        // Fetch trades where user is involved
        List<Trade> userTrades = tradeRepository.findByUserId(userId);

        if (userTrades == null || userTrades.isEmpty()) {
            return null;
        }

        List<PastTradeDTO> pastTrades = userTrades.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .map(trade -> {
                    PastTradeDTO pastTradeDTO = new PastTradeDTO();
                    pastTradeDTO.setUserId(trade.getUserId());
                    pastTradeDTO.setPrice(trade.getPrice());
                    pastTradeDTO.setQuantity(trade.getQuantity());
                    pastTradeDTO.setSide(trade.getSide());
                    pastTradeDTO.setCreatedAt(trade.getCreatedAt());
                    return pastTradeDTO;
                })
                .toList();

        return pastTrades;
    }

    public WalletDTO getWallet() {
        WalletDTO walletDTO = null;

        try {
            String url = walletServiceUrl + "getWallet"; // ensure this is the correct endpoint
            AuthCredentials creds = (AuthCredentials) SecurityContextHolder.getContext().getAuthentication()
                    .getCredentials();
            String deviceFingerprint = creds.getFingerprint();
            String jwt = creds.getJwt();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", "jwt=" + jwt); // Send JWT in cookie format
            headers.set("X-Device-Fingerprint", deviceFingerprint);
            headers.setContentType(MediaType.APPLICATION_JSON); // optional, since body is empty

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            ResponseEntity<WalletDTO> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    requestEntity,
                    WalletDTO.class);

            System.out.println(response.getBody());
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Wallet fetched successfully.");
                walletDTO = (WalletDTO) response.getBody();
            } else {
                System.err.println("Wallet fetch failed with status: " + response.getStatusCode());
                throw new RuntimeException("Failed to fetch wallet");
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch wallet: " + e.getMessage());
        }
        return walletDTO;
    }

    // public boolean checkWallet(OrderRequest orderRequest) {
    //     WalletDTO walletDTO = getWallet();
    //     if (walletDTO == null) {
    //         return false;
    //     }

    //     // Check if the user has enough balance
    //     if (orderRequest.getSide().equals("BUY")) {
    //         return walletDTO.getBalance() >= orderRequest.getPrice()*orderRequest.getQuantity();
    //     } else if (orderRequest.getSide().equals("SELL")) {
    //         return walletDTO.getGold() >= orderRequest.getQuantity();
    //     }
    //     return false;
    // }

    public boolean checkWallet(OrderRequest orderRequest) {
    WalletDTO walletDTO = getWallet();
    if (walletDTO == null) {
        return false;
    }

    String side = orderRequest.getSide();
    String type = orderRequest.getType();

    if ("BUY".equals(side)) {
        if ("LIMIT".equals(type)) {
            // For limit buy: user pays price * quantity
            double cost = orderRequest.getPrice() * orderRequest.getQuantity();
            return walletDTO.getBalance() >= cost;
        } else if ("MARKET".equals(type)) {
            
            // For market buy: you don't know exact price, so:
            // Option 1: Assume a max slippage or fetch current lowest ask
            // double estimatedPrice = fetchLTPOrBestAsk(); // Implement this
            // double estimatedCost = estimatedPrice * orderRequest.getQuantity();
            // return walletDTO.getBalance() >= estimatedCost;
            
            // Option 2: Assume a fixed price for market orders
            return true;   // Assuming user has enough balance for market buy, as price is not known
        }
    } else if ("SELL".equals(side)) {
        // For both limit and market sell, quantity of gold must be available
        return walletDTO.getGold() >= orderRequest.getQuantity();
    }

    return false;
}


}
