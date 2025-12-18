package io.goldexchange.auth_service.repository;

import io.goldexchange.auth_service.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for User entity operations.
 * Extends JpaRepository to provide standard CRUD operations.
 */
public interface AuthRepositoryWrapper extends JpaRepository<User, Long> {

    /**
     * Finds a user by their phone number.
     *
     * @param phoneNumber The phone number to search for.
     * @return An Optional containing the User if found, or empty otherwise.
     */
    Optional<User> findByPhoneNumber(String phoneNumber);
}
