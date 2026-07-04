package io.goldexchange.wallet_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * JPA Entity class representing a user's wallet in the system.
 * This entity stores the monetary balance and physical/digital gold holdings of a user.
 * It is mapped to the "wallets" table in the underlying relational database.
 */
@Entity
@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallets")
public class Wallet {

    /**
     * The primary key, auto-generated unique identifier for the wallet record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    /**
     * The unique identifier of the user who owns this wallet.
     * Typically acts as a foreign key linking to a User entity in an external Identity/User Service.
     */
    private Long userId;

    /**
     * The fiat currency balance available in the wallet (e.g., INR).
     * Used for purchasing gold or making fiat withdrawals.
     */
    private Double balance;

    /**
     * The total quantity of gold holdings in the wallet, typically measured in grams.
     * Represents the actual asset the user possesses.
     */
    private Double gold;

}
