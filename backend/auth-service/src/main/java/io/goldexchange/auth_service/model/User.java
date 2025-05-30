package io.goldexchange.auth_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String phoneNumber;
    private String secretKey;
    private String state;

    public User() {}

    public User(String userName, String phoneNumber, String secretKey, String state) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.secretKey = secretKey;
        this.state = state;
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getSecretKey() { return secretKey; }
    public void setSecretKey(String secretKey) { this.secretKey = secretKey; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
}
