package com.azael.taskapp.validation.password;

import com.azael.taskapp.persistence.dto.request.auth.RegisterRequestDto;
import com.azael.taskapp.persistence.dto.request.user.CreateUserRequestDto;
import com.azael.taskapp.persistence.dto.request.user.UpdateUserRequestDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        // Puedes hacer alguna inicialización si es necesario
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof RegisterRequestDto) {
            RegisterRequestDto registerRequestDto = (RegisterRequestDto) obj;

            // Compara las contraseñas
            if (!registerRequestDto.password().equals(registerRequestDto.confirmPassword())) {
                // Aquí asignamos el mensaje de error al campo `confirmPassword`
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Passwords do not match")
                        .addPropertyNode("confirmPassword") // O usa "password" si prefieres ese campo
                        .addConstraintViolation();
                return false; // La validación falla
            }
        }
        if (obj instanceof CreateUserRequestDto) {
            CreateUserRequestDto registerRequestDto = (CreateUserRequestDto) obj;

            // Compara las contraseñas
            if (!registerRequestDto.password().equals(registerRequestDto.confirmPassword())) {
                // Aquí asignamos el mensaje de error al campo `confirmPassword`
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Passwords do not match")
                        .addPropertyNode("confirmPassword") // O usa "password" si prefieres ese campo
                        .addConstraintViolation();
                return false; // La validación falla
            }
        }

        if (obj instanceof UpdateUserRequestDto) {
            UpdateUserRequestDto registerRequestDto = (UpdateUserRequestDto) obj;

            // Compara las contraseñas
            if (!registerRequestDto.password().equals(registerRequestDto.confirmPassword())) {
                // Aquí asignamos el mensaje de error al campo `confirmPassword`
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Passwords do not match")
                        .addPropertyNode("confirmPassword") // O usa "password" si prefieres ese campo
                        .addConstraintViolation();
                return false; // La validación falla
            }
        }
        return true; // Las contraseñas coinciden
    }
}