package com.azael.taskapp.validation.password;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Definir la anotación personalizada para la validación de la confirmación de la contraseña
@Documented
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class) // Se especifica el validador a usar
public @interface ValidPassword {
    String message() default "Passwords do not match"; // Mensaje por defecto
    Class<?>[] groups() default {}; // Grupos de validación
    Class<? extends Payload>[] payload() default {}; // Información adicional sobre la validación
}
