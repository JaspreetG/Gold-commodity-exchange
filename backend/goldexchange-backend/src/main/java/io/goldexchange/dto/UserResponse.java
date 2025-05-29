package io.goldexchange.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long userId;
    private String username;
    private double cashBalance;
    private double goldBalance;
}
