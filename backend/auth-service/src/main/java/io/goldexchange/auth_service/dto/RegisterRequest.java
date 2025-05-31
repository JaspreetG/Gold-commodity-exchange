package io.goldexchange.auth_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userName;
    private String phoneNumber;
}
