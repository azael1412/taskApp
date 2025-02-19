package com.azael.taskapp.validation.user.create;

import org.springframework.stereotype.Component;

import com.azael.taskapp.persistence.repositories.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class CreateUniqueEmailValidator implements ConstraintValidator<CreateUniqueEmail, String> {

    private final UserRepository userRepository;

    public CreateUniqueEmailValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && !userRepository.existsByEmail(email);
    }
}
