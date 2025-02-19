package com.azael.taskapp.persistence.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to access the system")
public record LoginRequestDto(
        @Schema(description = "Email", example = "johndoe@example.com", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Username is required!") String username,
        @Schema(description = "Password", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Password is mandatory") String password) {
}
