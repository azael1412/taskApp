package com.azael.taskapp.services.impl;

import org.springframework.stereotype.Component;

import com.azael.taskapp.persistence.entities.RefreshToken;
import com.azael.taskapp.persistence.entities.User;
import com.azael.taskapp.persistence.repositories.RefreshTokenRepository;
import com.azael.taskapp.services.RefreshTokenService;

@Component
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user, int expiryMinutes) {
        // Eliminar cualquier refresh token existente para el usuario
        deleteRefreshToken(user.getId());
        
        RefreshToken refreshToken = new RefreshToken(user, expiryMinutes);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyRefreshToken(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found!"));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token expired!");
        }

        return refreshToken;
    }

    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.deleteByUser(userId);
    }
}