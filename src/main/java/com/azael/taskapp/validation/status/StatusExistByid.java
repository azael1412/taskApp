package com.azael.taskapp.validation.status;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusExistByIdValidator.class)
@Documented
public @interface StatusExistByid {
    String message() default "Status does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

