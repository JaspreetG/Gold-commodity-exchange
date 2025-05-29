package io.goldexchange.service;

import io.goldexchange.entity.User;
import io.goldexchange.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }
}
