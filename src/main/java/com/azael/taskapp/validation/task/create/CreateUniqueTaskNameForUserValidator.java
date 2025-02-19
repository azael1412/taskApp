package com.azael.taskapp.validation.task.create;


import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.entities.User;
import com.azael.taskapp.persistence.repositories.TaskRepository;
import com.azael.taskapp.persistence.repositories.UserRepository;
// import com.azael.taskapp.services.AuthService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateUniqueTaskNameForUserValidator implements ConstraintValidator<CreateUniqueTaskNameForUser, String> {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String taskName, ConstraintValidatorContext context) {
        if (taskName == null || taskName.isEmpty()) {
            return true;  // Si el nombre de la tarea está vacío, no validamos aquí (puede validarse por otros medios)
        }
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         if (authentication == null || !authentication.isAuthenticated()) {
                throw new ServiceLogicException("No authenticated user");
        }
        String username = authentication.getName();
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return !taskRepository.existsByUserIdAndName(user.get().getId(), taskName);
        }
        return false;
    }
}