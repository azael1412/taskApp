package com.azael.taskapp.services;

import org.springframework.stereotype.Service;

import com.azael.taskapp.persistence.entities.RefreshToken;
import com.azael.taskapp.persistence.entities.User;

@Service
public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user, int expiryMinutes);
    RefreshToken verifyRefreshToken(String token);
    void deleteRefreshToken(Long userId);
}
