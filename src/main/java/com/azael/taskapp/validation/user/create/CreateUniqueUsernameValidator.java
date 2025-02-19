package com.azael.taskapp.validation.user.create;

import org.springframework.stereotype.Component;

import com.azael.taskapp.persistence.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class CreateUniqueUsernameValidator implements ConstraintValidator<CreateUniqueUsername, String> {

    private final UserRepository userRepository;

    public CreateUniqueUsernameValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return username != null && !userRepository.existsByUsername(username);
    }
}
