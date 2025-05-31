package io.goldexchange.auth_service.repository;

import io.goldexchange.auth_service.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepositoryWrapper extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
}
