package com.azael.taskapp.validation.task.create;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CreateUniqueTaskNameForUserValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateUniqueTaskNameForUser {
    String message() default "Task name must be unique for the user";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

