package com.azael.taskapp.validation.user.create;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CreateUniqueUsernameValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateUniqueUsername {
    String message() default "Username is already in use";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}