package io.goldexchange.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;
    private String username;
    private String password;
    private String totpSecret;
    private boolean totpEnabled;
    private double cashBalance;
    private double goldBalance;
}
