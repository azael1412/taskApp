package com.azael.taskapp.persistence.dto.request.auth;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request to refresh the user token")
public record RefreshTokenRequestDto(
        @Schema(description = "The refresh token used to obtain a new access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") @NotBlank(message = "Refresh token is required!") String refreshToken) {
}
