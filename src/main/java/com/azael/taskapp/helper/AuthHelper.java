package com.azael.taskapp.helper;

import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.entities.User;
import com.azael.taskapp.persistence.repositories.UserRepository;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthHelper {
    @Autowired
    private UserRepository userRepository;

    public User getCurrentUser() throws ServiceLogicException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);
            return user.orElse(null);
        } catch (Exception e) {
            throw new ServiceLogicException("Unexpected error occurred while getting user: " + e.getMessage());
        }
    }
}
