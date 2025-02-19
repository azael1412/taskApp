package com.azael.taskapp.validation.user.edit;

import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.persistence.repositories.UserRepository;

import java.util.Map;

@Component
public class UpdateUserValidator {
    private final UserRepository userRepository;

    public UpdateUserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Validar si el email ya está en uso por otro usuario (excepto por el mismo usuario)
    public void validateUniqueEmail(Long userId, String email) {
        if (userRepository.findByEmailAndIdNot(email, userId) != null) {
            Map<String, String> emailError = Map.of("message", "Registration failed: User already exists with email " + email);
            Map<String, Map<String, String>> errors = Map.of("email", emailError);
            throw new DataInvalidException("Validation failed", errors);
        }
    }

    // Validar si el nombre de usuario ya está en uso por otro usuario (excepto por el mismo usuario)
    public void validateUniqueUsername(Long userId, String username) {
        if (userRepository.findByUsernameAndIdNot(username, userId) != null) {
            Map<String, String> usernameError = Map.of("message", "Registration failed: User already exists with username " + username);
            Map<String, Map<String, String>> errors = Map.of("username", usernameError);
            throw new DataInvalidException("Validation failed", errors);
        }
    }
}
