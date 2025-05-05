package com.flowbank.service;

import com.flowbank.entity.User;
import com.flowbank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LogService logService;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(User user) {
        user.setId(UUID.randomUUID());
        User saved = userRepository.save(user);
        logService.logAction(saved, "Registered new user.");
        return saved;
    }
    public Optional<User> findByFullName(String fullName) {
        return userRepository.findAll().stream()
                .filter(u -> u.getFullName().equalsIgnoreCase(fullName))
                .findFirst();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

}