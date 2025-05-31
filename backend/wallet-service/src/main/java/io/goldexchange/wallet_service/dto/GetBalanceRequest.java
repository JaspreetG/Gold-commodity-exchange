package io.goldexchange.wallet_service.dto;

public class GetBalanceRequest {
    private Long userId;
    public GetBalanceRequest() {}
    public GetBalanceRequest(Long userId) { this.userId = userId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
}
