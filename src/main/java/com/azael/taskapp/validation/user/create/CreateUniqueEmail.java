package com.azael.taskapp.validation.user.create;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CreateUniqueEmailValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateUniqueEmail {
    String message() default "E-Mail is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}