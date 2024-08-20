package com.example.game_service_server.utils.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PlayerStatusValidator.class)
public @interface ValidPlayerStatus {
    String message() default "Invalid player status either should be ACTIVE or INACTIVE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
