package com.homs.account_rest_api.annotations;

import com.homs.account_rest_api.validator.TypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidType {
    String message() default "Invalid parameter type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String type();
}
