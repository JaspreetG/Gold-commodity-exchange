package io.goldexchange.wallet_service.dto;

public class AddMoneyRequest {
    private Long userId;
    private Double amount;

    public AddMoneyRequest() {}
    public AddMoneyRequest(Long userId, Double amount) {
        this.userId = userId;
        this.amount = amount;
    }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
