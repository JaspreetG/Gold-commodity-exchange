package io.goldexchange.wallet_service.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Entity representing a user's wallet.
 * Maps to the "wallets" table in the database.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wallets")
public class Wallet {

    /**
     * Unique identifier for the wallet.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    /**
     * Identifier of the user owning the wallet.
     */
    private Long userId;

    /**
     * The currency balance (e.g., INR).
     */
    private Double balance;

    /**
     * The gold balance (in grams).
     */
    private Double gold;

}
