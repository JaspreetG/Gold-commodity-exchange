package io.goldexchange.auth_service.dto;


import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long userId;
    private String userName;
    private String phoneNumber;

    @JsonIgnore
    private String secretKey;
    
    @JsonIgnore
    private String state;

}
