package com.azael.taskapp.persistence.dto.request.auth;

import com.azael.taskapp.validation.password.ValidPassword;
import com.azael.taskapp.validation.user.create.CreateUniqueEmail;
import com.azael.taskapp.validation.user.create.CreateUniqueUsername;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a user")
@ValidPassword(message = "Passwords do not match") // Anotación personalizada para validar contraseñas
public record RegisterRequestDto(
        @Schema(description = "User's full name", example = "John Doe") @NotBlank(message = "Name is mandatory") @Size(min = 3, max = 50, message = "Name should be between 3 to 50 characters") String name,
        @Schema(description = "Unique username for the user", example = "johndoe") @NotBlank(message = "Username is required!") @Size(min = 3, max = 20, message = "Username must have at least 3 characters!") @CreateUniqueUsername String username,
        @Schema(description = "User's email address", example = "johndoe@example.com") @Email(message = "Email is not in valid format!") @NotBlank(message = "Email is required!") @CreateUniqueEmail String email,
        @Schema(description = "Password for the user account", example = "password123") @NotBlank(message = "Password is mandatory") @Size(min = 6, max = 16, message = "Password should be between 6 to 16 characters") String password,
        @Schema(description = "Confirm password to validate the entered password", example = "password123") @NotBlank(message = "Confirm Password is mandatory") @Size(min = 6, max = 16, message = "Confirm Password should be between 6 to 16 characters") String confirmPassword,
        @Schema(description = "User's phone number", example = "1234567890") @NotBlank(message = "Phone number is required!") @Size(min = 10, max = 10, message = "Phone number must have 10 characters!") @Pattern(regexp = "^[0-9]*$", message = "Phone number must contain only digits") String phone) {
}
