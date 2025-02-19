package com.azael.taskapp.persistence.dto.request.user;

import com.azael.taskapp.validation.password.ValidPassword;
import com.azael.taskapp.validation.role.RoleExistById;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to update an existing user")
@ValidPassword(message = "Passwords do not match") // Anotación para validar contraseñas
public record UpdateUserRequestDto(

    @Schema(description = "The full name of the user", 
            example = "Jane Doe", 
            minLength = 3, 
            maxLength = 50)
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 50, message = "Name should be between 3 to 50 characters")
    String name,

    @Schema(description = "The username of the user", 
            example = "janedoe", 
            minLength = 3, 
            maxLength = 20)
    @NotBlank(message = "Username is required!")
    @Size(min = 3, max = 20, message = "Username should be between 3 to 20 characters!")
    String username,

    @Schema(description = "The email of the user", 
            example = "janedoe@example.com")
    @Email(message = "Email is not in valid format!")
    @NotBlank(message = "Email is required!")
    String email,

    @Schema(description = "The password of the user", 
            example = "NewPassword123", 
            minLength = 6, 
            maxLength = 16)
    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, max = 16, message = "Password should be between 6 to 16 characters")
    String password,

    @Schema(description = "Confirm password to ensure matching passwords", 
            example = "NewPassword123", 
            minLength = 6, 
            maxLength = 16)
    @NotBlank(message = "Confirm Password is mandatory")
    @Size(min = 6, max = 16, message = "Confirm Password should be between 6 to 16 characters")
    String confirmPassword,

    @Schema(description = "Indicates whether the user is active", 
            example = "true")
    boolean isActive,

    @Schema(description = "The phone number of the user", 
            example = "9876543210", 
            minLength = 10, 
            maxLength = 10)
    @NotBlank(message = "Phone number is required!")
    @Size(min = 10, max = 10, message = "Phone number must have 10 characters!")
    @Pattern(regexp="^[0-9]*$", message = "Phone number must contain only digits")
    String phone,

    @Schema(description = "The role ID associated with the user", 
            example = "2")
    @NotNull(message = "Role is mandatory")
    @RoleExistById
    Long roleId
) {}
