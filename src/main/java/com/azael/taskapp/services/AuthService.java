package com.azael.taskapp.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.azael.taskapp.exceptions.DataInvalidException;
import com.azael.taskapp.exceptions.InvalidAuthException;
import com.azael.taskapp.exceptions.ServiceLogicException;
import com.azael.taskapp.persistence.dto.request.auth.LoginRequestDto;
import com.azael.taskapp.persistence.dto.request.auth.RefreshTokenRequestDto;
import com.azael.taskapp.persistence.dto.request.auth.RegisterRequestDto;
import com.azael.taskapp.persistence.dto.response.auth.LoginResponseDto;
import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;
import com.azael.taskapp.persistence.entities.User;

import jakarta.validation.ConstraintViolationException;

@Service
public interface AuthService {
    LoginResponseDto login(LoginRequestDto request) throws BadCredentialsException, InvalidAuthException, ServiceLogicException;
    //RegisterResponseDto register(RegisterRequestDto request);
    UserResponseDto register(RegisterRequestDto request) throws ServiceLogicException, DataInvalidException, ConstraintViolationException;
    User me();
    LoginResponseDto refreshToken(String authHeader, RefreshTokenRequestDto request) throws ServiceLogicException;
    void logout(String authHeader) throws ServiceLogicException;
}
