package com.azael.taskapp.persistence.dto.response.auth;

import com.azael.taskapp.persistence.dto.response.user.UserResponseDto;

public record LoginResponseDto(
String accessToken, String tokenType, String refreshToken,String expiresIn, UserResponseDto user) {
}
